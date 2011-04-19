package com.github.peholmst.neo4jvaadindemo.article3.util;

import java.lang.reflect.Method;

/**
 *
 * @author peholmst
 */
class NestedProperty extends SimpleProperty {
    final Method[] getterMethodChain;

    protected NestedProperty(Object item, Method...getterMethodChain) {
        super(item, getterMethodChain[getterMethodChain.length -1]);
        this.getterMethodChain = getterMethodChain;
    }

    protected NestedProperty(Object item, Method setterMethod, Method...getterMethodChain) {
        super(item, getterMethodChain[getterMethodChain.length -1], setterMethod);
        this.getterMethodChain = getterMethodChain;
    }
    
    public static NestedProperty readOnlyNestedProperty(Object item, Method...getterMethodChain) {
        assert getterMethodChain != null : "getterMethodChain must not be null";
        assert getterMethodChain.length > 0 : "getterMethodChain must contain at least one element";
        return new NestedProperty(item, getterMethodChain);
    }
    
    public static NestedProperty writableNestedProperty(Object item, Method setterMethod, Method... getterMethodChain) {
        assert setterMethod != null : "setterMethod must not be null";
        assert getterMethodChain != null : "getterMethodChain must not be null";
        assert getterMethodChain.length > 0 : "getterMethodChain must contain at least one element";
        return new NestedProperty(item, setterMethod, getterMethodChain);
    }

    @Override
    protected Object getInvocationTarget() {
        try {
            Object target = super.getInvocationTarget();
            for (int i = 0; i < getterMethodChain.length -1; ++i) {
                Method getter = getterMethodChain[i];
                target = getter.invoke(target);
                if (target == null) {
                    return null;
                }
            }
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Could not access parent getter");
        }
    }
    
}
