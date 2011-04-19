package com.github.peholmst.neo4jvaadindemo.article3.util;

import com.vaadin.data.Property;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test-case for {@link NestedProperty}.
 * 
 * @author Petter Holmström
 */
public class NestedPropertyTest {
 
    static class TestClass {
        
        private String stringValue;
        
        private TestClass testValue;

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        public TestClass getTestValue() {
            return testValue;
        }

        public void setTestValue(TestClass testValue) {
            this.testValue = testValue;
        }                     
    }
    TestClass item;
    Method stringValueGetter;
    Method stringValueSetter;
    Method testValueGetter;
    Method testValueSetter;
    
    @Before
    public void setUp() throws Exception {
        item = new TestClass();
        item.stringValue = "hello";
        item.testValue = new TestClass();
        item.testValue.stringValue = "world";
        
        stringValueGetter = TestClass.class.getMethod("getStringValue");
        stringValueSetter = TestClass.class.getMethod("setStringValue", String.class);
        testValueGetter = TestClass.class.getMethod("getTestValue");
        testValueSetter = TestClass.class.getMethod("setTestValue", TestClass.class);
    }
    
    @Test
    public void readOnlyNestedProperty_noNesting() {
        Property property = NestedProperty.readOnlyNestedProperty(item, stringValueGetter);
        assertSame(String.class, property.getType());
        assertTrue(property.isReadOnly());
    }  
    
    @Test
    public void readOnlyNestedProperty_noNesting_getValue() {
        Property property = NestedProperty.readOnlyNestedProperty(item, stringValueGetter);
        assertEquals("hello", property.getValue());
    }
    
    @Test
    public void readOnlyNestedProperty_oneLevelNesting() {
        Property property = NestedProperty.readOnlyNestedProperty(item, testValueGetter, stringValueGetter);
        assertSame(String.class, property.getType());
        assertTrue(property.isReadOnly());        
    }
    
    @Test
    public void readOnlyNestedProperty_oneLevelNesting_getValue() {
        Property property = NestedProperty.readOnlyNestedProperty(item, testValueGetter, stringValueGetter);
        assertEquals("world", property.getValue());
    }    
    
    @Test
    public void readOnlyNestedProperty_twoLevelNesting() {
        Property property = NestedProperty.readOnlyNestedProperty(item, testValueGetter, testValueGetter, stringValueGetter);
        assertSame(String.class, property.getType());
        assertTrue(property.isReadOnly());        
    }

    @Test
    public void readOnlyNestedProperty_twoLevelNesting_getValue_NullTarget() {
        Property property = NestedProperty.readOnlyNestedProperty(item, testValueGetter, testValueGetter, stringValueGetter);
        assertNull(property.getValue());
    }    

    @Test
    public void readOnlyNestedProperty_twoLevelNesting_getValue_NullTargetInTheMiddle() {
        item.testValue = null;
        Property property = NestedProperty.readOnlyNestedProperty(item, testValueGetter, testValueGetter, stringValueGetter);
        assertNull(property.getValue());
    } 
    
    @Test
    public void writableNestedProperty_noNesting() {
        Property property = NestedProperty.writableNestedProperty(item, stringValueSetter, stringValueGetter);
        assertSame(String.class, property.getType());
        assertFalse(property.isReadOnly());        
    }
    
    @Test
    public void writableNestedProperty_noNesting_setValue() {
        Property property = NestedProperty.writableNestedProperty(item, stringValueSetter, stringValueGetter);
        property.setValue("new value");
        assertEquals("new value", item.stringValue);
    }
    
    @Test
    public void writableNestedProperty_oneLevelNesting_setValue() {
        Property property = NestedProperty.writableNestedProperty(item, stringValueSetter, testValueGetter, stringValueGetter);
        property.setValue("new value");
        assertEquals("new value", item.testValue.stringValue);
    }    

    @Test(expected=Property.ReadOnlyException.class)
    public void writableNestedProperty_oneLevelNesting_setValue_NullTarget() {
        item.testValue = null;
        Property property = NestedProperty.writableNestedProperty(item, stringValueSetter, testValueGetter, stringValueGetter);
        property.setValue("new value");
    }  
    
    @Test(expected=Property.ReadOnlyException.class)
    public void writableNestedProperty_twoLevelNesting_setValue_NullTargetInTheMiddle() {
        item.testValue = null;
        Property property = NestedProperty.readOnlyNestedProperty(item, testValueGetter, testValueGetter, testValueGetter, stringValueGetter);
        assertNull(property.getValue());
        property.setValue("new value");
    } 
    
}
