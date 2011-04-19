package com.github.peholmst.neo4jvaadindemo.article3.util;

import com.vaadin.data.Property;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

/**
 * Utility class for working with objects that use the {@link ItemProperty} annotation.
 * 
 * @author Petter Holmström
 */
public final class ItemPropertyUtil {

    /**
     * Returns a collection of the names of all public JavaBean properties of the specified class that
     * have been annotated with the {@link ItemProperty} annotation. If <code>includeNestedProperties</code> is true,
     * nested property names (i.e. "xx.yy.zz") will also be included.
     */
    public static Collection<String> getItemPropertyIdsFromClass(Class<?> itemClass, boolean includeNestedProperties) {
        return new ItemPropertyIdExtractor(itemClass, includeNestedProperties).getItemPropertyIds();
    }

    private static class ItemPropertyIdExtractor {

        private final boolean includeNestedProperties;
        private final Class<?> itemClass;

        private ItemPropertyIdExtractor(Class<?> itemClass, boolean includeNestedProperties) {
            this.itemClass = itemClass;
            this.includeNestedProperties = includeNestedProperties;
        }

        private Collection<String> getItemPropertyIds() {
            HashSet<String> propertyIds = new HashSet<String>();
            for (Method method : itemClass.getMethods()) {
                addPropertyNamesFromMethod(method, propertyIds);
            }
            return propertyIds;
        }

        private void addPropertyNamesFromMethod(Method method, HashSet<String> propertyIds) {
            ItemProperty itemPropertyMetadata = method.getAnnotation(ItemProperty.class);
            if (itemPropertyMetadata != null) {
                String propertyName = getPropertyNameFromMethod(method);
                propertyIds.add(propertyName);
                if (includeNestedProperties) {
                    addNestedProperties(propertyName, itemPropertyMetadata, propertyIds);
                }
            }

        }

        private String getPropertyNameFromMethod(Method method) {
            String baseName;
            if (method.getName().startsWith("is")) {
                baseName = method.getName().substring(2);
            } else if (method.getName().startsWith("get")) {
                baseName = method.getName().substring(3);
            } else {
                throw new IllegalArgumentException("Method is not a getter");
            }
            return Introspector.decapitalize(baseName);
        }

        private void addNestedProperties(String baseName, ItemProperty itemPropertyMetadata, HashSet<String> itemIds) {
            for (NestedItemProperty nestedProperty : itemPropertyMetadata.nestedProperties()) {
                StringBuilder sb = new StringBuilder();
                sb.append(baseName);
                sb.append(".");
                sb.append(nestedProperty.propertyName());
                itemIds.add(sb.toString());
            }
        }
    }

    /**
     * Creates a new {@link Property} instance for the specified property name
     * and item. Nested properties are supported.
     */
    public static Property createProperty(String propertyName, Object item) {
        assert item != null : "item must not be null";
        assert propertyName != null : "propertyName must not be null";
        if (propertyNameIsNested(propertyName)) {
            return createNestedProperty(propertyName, item);
        } else {
            return createSimpleProperty(propertyName, item);
        }
    }

    private static boolean propertyNameIsNested(String propertyName) {
        return propertyName.contains(".");
    }

    private static Property createNestedProperty(String propertyName, Object item) {
        return new NestedItemPropertyCreator(propertyName, item.getClass()).create(item);
    }

    private static class NestedItemPropertyCreator {

        private final String propertyName;
        private final Class<?> itemClass;
        private final String[] propertyNameParts;
        private final Method[] getterMethodChain;
        private final NestedItemProperty metadata;
        private final Method setterMethod;

        private NestedItemPropertyCreator(String propertyName, Class<?> itemClass) {
            this.propertyName = propertyName;
            this.itemClass = itemClass;
            this.propertyNameParts = propertyName.split("\\.");
            this.getterMethodChain = constructGetterMethodChain();
            this.metadata = extractMetadata();
            this.setterMethod = extractSetterMethod();
        
        }

        private Method[] constructGetterMethodChain() {
            Method[] chain = new Method[propertyNameParts.length];
            Class<?> ownerClass = itemClass;
            for (int i = 0; i < propertyNameParts.length; ++i) {
                String propertyNamePart = propertyNameParts[i];
                Method getter = findGetterMethod(propertyNamePart, ownerClass);
                chain[i] = getter;
                ownerClass = getter.getReturnType();
            }
            return chain;
        }

        private NestedItemProperty extractMetadata() {
            ItemProperty rootPropertyMetadata = getRootPropertyGetter().getAnnotation(ItemProperty.class);
            if (rootPropertyMetadata == null) {
                throw new IllegalArgumentException("No @ItemProperty annotation found");
            }
            NestedItemProperty nestedMetadata = extractNestedItemPropertyMetadata(rootPropertyMetadata);
            if (nestedMetadata == null) {
                throw new IllegalArgumentException("No @NestedItemProperty annotation found");
            }
            return nestedMetadata;
        }

        private NestedItemProperty extractNestedItemPropertyMetadata(ItemProperty rootPropertyMetadata) {
            String expectedPropertyName = propertyName.substring(getRootPropertyName().length() + 1);
            for (NestedItemProperty nestedItemProperty : rootPropertyMetadata.nestedProperties()) {
                if (expectedPropertyName.equals(nestedItemProperty.propertyName())) {
                    return nestedItemProperty;
                }
            }
            return null;
        }

        private Method extractSetterMethod() {
            String propertyName = propertyNameParts[propertyNameParts.length - 1];
            Method propertyGetter = getterMethodChain[getterMethodChain.length -1];
            Class<?> propertyType = propertyGetter.getReturnType();
            Class<?> ownerClass = propertyGetter.getDeclaringClass();

            return findSetterMethod(propertyName, propertyType, ownerClass);
        }

        private NestedProperty create(Object item) {
            if (metadata.readOnly() || setterMethod == null) {
                return NestedProperty.readOnlyNestedProperty(item, getterMethodChain);
            } else {
                return NestedProperty.writableNestedProperty(item, setterMethod, getterMethodChain);
            
            }
        }

        private String getRootPropertyName() {
            return propertyNameParts[0];
        }

        private Method getRootPropertyGetter() {
            return getterMethodChain[0];
        }
    }

    private static Property createSimpleProperty(String propertyName, Object item) {
        Method getterMethod = findGetterMethod(propertyName, item.getClass());
        Method setterMethod = findSetterMethod(propertyName, getterMethod.getReturnType(), item.getClass());
        if (setterMethod != null) {
            return SimpleProperty.writableProperty(item, getterMethod, setterMethod);
        } else {
            return SimpleProperty.readOnlyProperty(item, getterMethod);
        }
    }

    private static String capitalize(String original) {
        if (original.isEmpty()) {
            return original;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(original.substring(0, 1).toUpperCase());
        sb.append(original.substring(1));
        return sb.toString();
    }

    private static Method findGetterMethod(String propertyName, Class<?> itemClass) {
        try {
            String capitalizedPropertyName = capitalize(propertyName);
            Method getterMethod;
            try {
                getterMethod = itemClass.getMethod("get" + capitalizedPropertyName);
            } catch (NoSuchMethodException e) {
                getterMethod = itemClass.getMethod("is" + capitalizedPropertyName);
            }
            return getterMethod;
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Could not find getter method for property " + propertyName);
        }
    }

    private static Method findSetterMethod(String propertyName, Class<?> propertyType, Class<?> itemClass) {
        try {
            String capitalizedPropertyName = capitalize(propertyName);
            Method setterMethod = itemClass.getMethod("set" + capitalizedPropertyName, propertyType);
            return setterMethod;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
