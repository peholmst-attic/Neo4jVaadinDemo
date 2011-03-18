package com.github.peholmst.neo4jvaadindemo.article3.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ItemProperty {

    NestedItemProperty[] nestedProperties() default {};
}
