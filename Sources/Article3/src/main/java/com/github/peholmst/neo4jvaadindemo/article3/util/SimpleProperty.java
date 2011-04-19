package com.github.peholmst.neo4jvaadindemo.article3.util;

import java.lang.reflect.Method;

/**
 * A {@link com.vaadin.data.Property} implementation that accesses a JavaBean property
 * of an object using its getter and setter methods.
 * 
 * @author peholmst
 */
class SimpleProperty extends AbstractProperty {
    final Method getterMethod;
    final Method setterMethod;
    final Object item;
    private boolean readOnly = false;

    protected SimpleProperty(Object item, Method getterMethod, Method setterMethod) {
        this.getterMethod = getterMethod;
        this.setterMethod = setterMethod;
        this.item = item;
    }

    protected SimpleProperty(Object item, Method getterMethod) {
        this(item, getterMethod, null);
    }

    public static SimpleProperty readOnlyProperty(Object item, Method getterMethod) {
        assert getterMethod != null : "getterMethod must not be null";
        return new SimpleProperty(item, getterMethod);
    }
    
    public static SimpleProperty writableProperty(Object item, Method getterMethod, Method setterMethod) {
        assert getterMethod != null : "getterMethod must not be null";
        assert setterMethod != null : "setterMethod must not be null";
        return new SimpleProperty(item, getterMethod, setterMethod);
    }
    
    @Override
    public Object getValue() {
        Object target = getInvocationTarget();
        if (target == null) {
            return null;
        }
        try {
            return getterMethod.invoke(target);
        } catch (Exception e) {
            throw new RuntimeException("Could not access value", e);
        }
    }

    protected Object getInvocationTarget() {
        return item;
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
        if (isReadOnly()) {
            throw new ReadOnlyException();
        }
        try {
            setterMethod.invoke(getInvocationTarget(), newValue);
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
        return setterMethod == null || getInvocationTarget() == null || readOnly;
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
