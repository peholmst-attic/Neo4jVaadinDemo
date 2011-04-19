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

    static class ParentItem {

        private NestedItem nestedProperty;
        private NestedItem anotherNestedProperty;

        @ItemProperty(nestedProperties = {
            @NestedItemProperty(propertyName = "writableProperty"),
            @NestedItemProperty(propertyName = "readOnlyProperty")})
        public NestedItem getNestedProperty() {
            return nestedProperty;
        }

        public void setNestedProperty(NestedItem value) {
            this.nestedProperty = value;
        }

        @ItemProperty(nestedProperties = {
            @NestedItemProperty(propertyName = "writableProperty", readOnly = false),
            @NestedItemProperty(propertyName = "readOnlyProperty", readOnly = false)})
        public NestedItem getAnotherNestedProperty() {
            return anotherNestedProperty;
        }

        public void setAnotherNestedProperty(NestedItem value) {
            this.anotherNestedProperty = value;
        }
    }

    static class NestedItem {

        private String writableProperty;

        @ItemProperty
        public String getWritableProperty() {
            return writableProperty;
        }

        public void setWritableProperty(String value) {
            this.writableProperty = value;
        }

        @ItemProperty
        public String getReadOnlyProperty() {
            return "ReadOnlyProperty";
        }
    }

    @Test
    public void getItemPropertyIdsFromClass_ExcludingNestedProperties() {
        Collection<String> propertyIds = ItemPropertyUtil.getItemPropertyIdsFromClass(ParentItem.class, false);
        assertTrue(propertyIds.contains("nestedProperty"));
        assertTrue(propertyIds.contains("anotherNestedProperty"));
        assertEquals(2, propertyIds.size());
    }

    @Test
    public void getItemPropertyIdsFromClass_IncludingNestedProperties() {
        Collection<String> propertyIds = ItemPropertyUtil.getItemPropertyIdsFromClass(ParentItem.class, true);
        assertTrue(propertyIds.contains("nestedProperty"));
        assertTrue(propertyIds.contains("anotherNestedProperty"));
        assertTrue(propertyIds.contains("nestedProperty.writableProperty"));
        assertTrue(propertyIds.contains("nestedProperty.readOnlyProperty"));
        assertTrue(propertyIds.contains("anotherNestedProperty.writableProperty"));
        assertTrue(propertyIds.contains("anotherNestedProperty.readOnlyProperty"));
        assertEquals(6, propertyIds.size());
    }

    @Test
    public void createProperty_SimpleProperty() {
        NestedItem item = new NestedItem();
        Property property = ItemPropertyUtil.createProperty("writableProperty", item);
        assertSame(String.class, property.getType());
        assertFalse(property.isReadOnly()); 
    }
    
    @Test
    public void createProperty_SimpleProperty_setValue() {
        NestedItem item = new NestedItem();
        Property property = ItemPropertyUtil.createProperty("writableProperty", item);
        property.setValue("hello");
        assertEquals("hello", item.writableProperty);
    }    
    
    @Test
    public void createProperty_SimpleProperty_getValue() {
        NestedItem item = new NestedItem();
        Property property = ItemPropertyUtil.createProperty("writableProperty", item);
        item.writableProperty = "hello";
        assertEquals("hello", property.getValue());
    }  
    
    @Test
    public void createProperty_SimpleProperty_readOnly() {
        NestedItem item = new NestedItem();
        Property property = ItemPropertyUtil.createProperty("readOnlyProperty", item);
        assertTrue(property.isReadOnly()); 
    }
    
    
    @Test
    public void createProperty_NestedProperty() {
        ParentItem item = new ParentItem();
        item.nestedProperty = new NestedItem();
        Property property = ItemPropertyUtil.createProperty("nestedProperty.writableProperty", item);
        assertSame(String.class, property.getType());
        assertTrue(property.isReadOnly());
    }
    
}
