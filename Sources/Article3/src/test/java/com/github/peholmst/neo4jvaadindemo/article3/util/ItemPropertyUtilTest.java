package com.github.peholmst.neo4jvaadindemo.article3.util;

import com.vaadin.data.Property;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for {@link ItemPropertyUtil}.
 * 
 * @author Petter Holmström
 */
public class ItemPropertyUtilTest {

    static class Parent {

        private Child child;
        private Child anotherchild;

        @ItemProperty(nestedProperties = {
            @NestedItemProperty(propertyName = "name"),
            @NestedItemProperty(propertyName = "upperCaseName"),
            @NestedItemProperty(propertyName = "grandchild.name"),
            @NestedItemProperty(propertyName = "grandchild.lowerCaseName")})
        public Child getChild() {
            return child;
        }

        public void setChild(Child value) {
            this.child = value;
        }

        @ItemProperty(nestedProperties = {
            @NestedItemProperty(propertyName = "name", readOnly = false),
            @NestedItemProperty(propertyName = "upperCaseName", readOnly = false)})
        public Child getAnotherChild() {
            return anotherchild;
        }

        public void setAnotherChild(Child value) {
            this.anotherchild = value;
        }
    }

    static class Child {

        private String name;
        private Grandchild grandchild;

        @ItemProperty
        public String getName() {
            return name;
        }

        public void setName(String value) {
            this.name = value;
        }

        @ItemProperty
        public Grandchild getGrandchild() {
            return grandchild;
        }

        public void setGrandchild(Grandchild grandchild) {
            this.grandchild = grandchild;
        }

        @ItemProperty
        public String getUpperCaseName() {
            return getName().toUpperCase();
        }
    }

    static class Grandchild {

        private String name;

        @ItemProperty
        public String getName() {
            return name;
        }

        public void setName(String value) {
            this.name = value;
        }

        @ItemProperty
        public String getLowerCaseName() {
            return getName().toLowerCase();
        }
    }

    @Test
    public void getItemPropertyIdsFromClass_ExcludingNestedProperties() {
        Collection<String> propertyIds = ItemPropertyUtil.getItemPropertyIdsFromClass(Parent.class, false);
        assertTrue(propertyIds.contains("child"));
        assertTrue(propertyIds.contains("anotherChild"));
        assertEquals(2, propertyIds.size());
    }

    @Test
    public void getItemPropertyIdsFromClass_IncludingNestedProperties() {
        Collection<String> propertyIds = ItemPropertyUtil.getItemPropertyIdsFromClass(Parent.class, true);
        assertTrue(propertyIds.contains("child"));
        assertTrue(propertyIds.contains("anotherChild"));
        assertTrue(propertyIds.contains("child.name"));
        assertTrue(propertyIds.contains("child.upperCaseName"));
        assertTrue(propertyIds.contains("child.grandchild.name"));
        assertTrue(propertyIds.contains("child.grandchild.lowerCaseName"));
        assertTrue(propertyIds.contains("anotherChild.name"));
        assertTrue(propertyIds.contains("anotherChild.upperCaseName"));
        assertEquals(8, propertyIds.size());
    }

    @Test
    public void createProperty_SimpleProperty() {
        Child item = new Child();
        Property property = ItemPropertyUtil.createProperty("name", item);
        assertSame(String.class, property.getType());
        assertFalse(property.isReadOnly());
    }

    @Test
    public void createProperty_SimpleProperty_setValue() {
        Child item = new Child();
        Property property = ItemPropertyUtil.createProperty("name", item);
        property.setValue("hello");
        assertEquals("hello", item.name);
    }

    @Test
    public void createProperty_SimpleProperty_getValue() {
        Child item = new Child();
        Property property = ItemPropertyUtil.createProperty("name", item);
        item.name = "hello";
        assertEquals("hello", property.getValue());
    }

    @Test
    public void createProperty_SimpleProperty_readOnly() {
        Child item = new Child();
        Property property = ItemPropertyUtil.createProperty("upperCaseName", item);
        assertTrue(property.isReadOnly());
    }

    @Test
    public void createProperty_NestedProperty_readOnlyByDefault() {
        Parent parent = new Parent();
        Child child = new Child();
        parent.child = child;
        Grandchild grandchild = new Grandchild();
        child.grandchild = grandchild;
        grandchild.name = "Hello";

        Property property = ItemPropertyUtil.createProperty("child.grandchild.name", parent);

        assertTrue(property.isReadOnly());
        assertEquals("Hello", property.getValue());
    }

    @Test
    public void createProperty_NestedProperty_writableByConfiguration() {
        Parent parent = new Parent();
        Child child = new Child();
        parent.anotherchild = child;

        Property property = ItemPropertyUtil.createProperty("anotherChild.name", parent);
        property.setValue("Hello");

        assertFalse(property.isReadOnly());
        assertEquals("Hello", child.name);
    }

    @Test
    public void createProperty_NestedProperty_readOnlyByImplementationWritableByConfiguration() {
        Parent parent = new Parent();
        Child child = new Child();
        parent.anotherchild = child;

        Property property = ItemPropertyUtil.createProperty("anotherChild.upperCaseName", parent);
        assertTrue(property.isReadOnly());
    }
}
