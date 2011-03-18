package com.github.peholmst.neo4jvaadindemo.article3.util;

import com.github.peholmst.neo4jvaadindemo.article3.util.ListenerList.ListenerNotifier;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;

/**
 * TODO Document me!
 * 
 * @author peholmst
 */
public final class ItemPropertyUtil {

    private ItemPropertyUtil() {
    }

    /**
     * 
     * @param itemClass
     * @param includeNestedProperties
     * @return 
     */
    public static Collection<String> getItemPropertyIdsFromClass(Class<?> itemClass, boolean includeNestedProperties) {
        HashSet<String> itemIds = new HashSet<String>();
        for (Method method : itemClass.getMethods()) {
            ItemProperty itemPropertyMetadata = method.getAnnotation(ItemProperty.class);
            if (itemPropertyMetadata != null) {
                String propertyName = getPropertyNameFromMethod(method);
                itemIds.add(propertyName);
                if (includeNestedProperties) {
                    addNestedProperties(propertyName, itemPropertyMetadata, itemIds);
                }
            }
        }
        return itemIds;
    }

    private static String getPropertyNameFromMethod(Method method) {
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

    private static void addNestedProperties(String baseName, ItemProperty itemPropertyMetadata, HashSet<String> itemIds) {
        for (NestedItemProperty nestedProperty : itemPropertyMetadata.nestedProperties()) {
            StringBuilder sb = new StringBuilder();
            sb.append(baseName);
            sb.append(".");
            sb.append(nestedProperty.propertyName());
            itemIds.add(sb.toString());
        }
    }

    /**
     * 
     * @param propertyName
     * @param item
     * @return 
     */
    public static Property createProperty(String propertyName, Object item) {
        if (propertyName.contains(".")) {
            return createNestedProperty(propertyName, item);
        } else {
            return createSimpleProperty(propertyName, item);
        }
    }

    private static abstract class AbstractProperty implements Property, Property.ReadOnlyStatusChangeNotifier, Property.ValueChangeNotifier {

        private final ListenerList<ReadOnlyStatusChangeListener> readOnlyListeners = new ListenerList<ReadOnlyStatusChangeListener>();
        private final ListenerList<ValueChangeListener> valueChangeListeners = new ListenerList<ValueChangeListener>();

        @Override
        public final void addListener(ReadOnlyStatusChangeListener listener) {
            readOnlyListeners.add(listener);
        }

        @Override
        public final void removeListener(ReadOnlyStatusChangeListener listener) {
            readOnlyListeners.remove(listener);
        }

        @Override
        public final void addListener(ValueChangeListener listener) {
            valueChangeListeners.add(listener);
        }

        @Override
        public final void removeListener(ValueChangeListener listener) {
            valueChangeListeners.remove(listener);
        }

        protected void fireValueChangeEvent() {
            final ValueChangeEvent event = new ValueChangeEvent() {

                @Override
                public Property getProperty() {
                    return AbstractProperty.this;
                }
            };
            valueChangeListeners.notifyListeners(new ListenerNotifier<ValueChangeListener>() {

                @Override
                public void notifyListener(ValueChangeListener listener) {
                    listener.valueChange(event);
                }
            });
        }

        protected void fireReadOnlyStatusChangeEvent() {
            final ReadOnlyStatusChangeEvent event = new ReadOnlyStatusChangeEvent() {

                @Override
                public Property getProperty() {
                    return AbstractProperty.this;
                }
            };
            readOnlyListeners.notifyListeners(new ListenerNotifier<ReadOnlyStatusChangeListener>() {

                @Override
                public void notifyListener(ReadOnlyStatusChangeListener listener) {
                    listener.readOnlyStatusChange(event);
                }
            });
        }
    }

    private static class SimpleProperty extends AbstractProperty {

        private final Method getterMethod;
        private final Method setterMethod;
        private final Object item;
        private boolean readOnly = false;

        public SimpleProperty(Object item, Method getterMethod, Method setterMethod) {
            this.getterMethod = getterMethod;
            this.setterMethod = setterMethod;
            this.item = item;
        }

        public SimpleProperty(Object item, Method getterMethod) {
            this(item, getterMethod, null);
        }

        @Override
        public Object getValue() {
            Object target = getItem();
            if (target == null) {
                return null;
            }
            try {
                return getterMethod.invoke(target);
            } catch (Exception e) {
                throw new RuntimeException("Could not access value", e);
            }
        }
        
        protected Object getItem() {
            return item;
        }

        @Override
        public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
            if (isReadOnly()) {
                throw new ReadOnlyException();
            }
            try {
                setterMethod.invoke(getItem(), newValue);
                fireValueChangeEvent();
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }

        @Override
        public Class<?> getType() {
            return getterMethod.getReturnType();
        }

        @Override
        public boolean isReadOnly() {
            return setterMethod == null || getItem() == null || readOnly;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            if (setterMethod == null) {
                throw new UnsupportedOperationException("No setter method available");
            }
            readOnly = newStatus;
            fireReadOnlyStatusChangeEvent();
        }
    }
    
    private static class NestedProperty extends SimpleProperty {

        private final Method parentGetter;
        
        public NestedProperty(Object item, Method parentGetter, Method getterMethod) {
            super(item, getterMethod);
            this.parentGetter = parentGetter;
        }

        public NestedProperty(Object item, Method parentGetter, Method getterMethod, Method setterMethod) {
            super(item, getterMethod, setterMethod);
            this.parentGetter = parentGetter;
        }               
                
        @Override
        protected Object getItem() {
            try {
                return parentGetter.invoke(super.getItem());
            } catch (Exception e) {
                throw new RuntimeException("Could not access parent getter");
            }
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

    private static Method findGetterMethod(String propertyName, Class<?> itemClass) throws NoSuchMethodException {
        String capitalizedPropertyName = capitalize(propertyName);
        Method getterMethod;
        try {
            getterMethod = itemClass.getMethod("get" + capitalizedPropertyName);
        } catch (NoSuchMethodException e) {
            getterMethod = itemClass.getMethod("is" + capitalizedPropertyName);
        }
        return getterMethod;
    }

    private static Method findSetterMethod(String propertyName, Class<?> propertyType, Class<?> itemClass) throws NoSuchMethodException {
        String capitalizedPropertyName = capitalize(propertyName);
        Method setterMethod = itemClass.getMethod("set" + capitalizedPropertyName, propertyType);
        return setterMethod;
    }

    private static Property createSimpleProperty(String propertyName, Object item) {
        try {
            Method getterMethod = findGetterMethod(propertyName, item.getClass());
            try {
                Method setterMethod = findSetterMethod(propertyName, getterMethod.getReturnType(), item.getClass());
                return new SimpleProperty(item, getterMethod, setterMethod);
            } catch (NoSuchMethodException e) {
                return new SimpleProperty(item, getterMethod);
            }
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Could not find getter method for property");
        }
    }

    private static Property createNestedProperty(String propertyName, Object item) {
        // TODO This method could use some cleaning up
        int indexOfSeparator = propertyName.indexOf(".");
        String parentProperty = propertyName.substring(0, indexOfSeparator);
        String nestedProperty = propertyName.substring(indexOfSeparator +1);
        try {
            Method parentGetter = findGetterMethod(parentProperty, item.getClass());
            ItemProperty metadata = parentGetter.getAnnotation(ItemProperty.class);
            if (metadata == null) {
                throw new IllegalArgumentException("No @ItemProperty annotation found");
            }
            NestedItemProperty nestedMetadata = getNestedItemPropertyMetadata(nestedProperty, metadata);
            if (nestedMetadata == null) {
                throw new IllegalArgumentException("No @NestedItemProperty annotation found");
            }
            
            Method getterMethod = findGetterMethod(nestedProperty, parentGetter.getReturnType());
            if (nestedMetadata.readOnly()) {
                return new NestedProperty(item, parentGetter, getterMethod);
            } else {
                try {
                    Method setterMethod = findSetterMethod(nestedProperty, getterMethod.getReturnType(), parentGetter.getReturnType());
                    return new NestedProperty(item, parentGetter, getterMethod, setterMethod);
                } catch (NoSuchMethodException e) {
                    return new NestedProperty(item, parentGetter, getterMethod);                    
                }
            }            
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Could not find getter method for property");
        }
    }
    
   private static NestedItemProperty getNestedItemPropertyMetadata(String propertyName, ItemProperty parentMetadata) {
       for (NestedItemProperty nestedItemProperty : parentMetadata.nestedProperties()) {
           if (propertyName.equals(nestedItemProperty.propertyName())) {
               return nestedItemProperty;
           }
       }
       return null;
   }
}
