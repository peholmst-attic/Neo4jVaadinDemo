package com.github.peholmst.neo4jvaadindemo.article3.util;

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
            @NestedItemProperty(propertyName = "writableProperty", readOnly=false),
            @NestedItemProperty(propertyName = "readOnlyProperty", readOnly=false)})
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
}
