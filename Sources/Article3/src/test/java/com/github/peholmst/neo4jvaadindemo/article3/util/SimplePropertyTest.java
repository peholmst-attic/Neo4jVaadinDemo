package com.github.peholmst.neo4jvaadindemo.article3.util;

import com.vaadin.data.Property.ReadOnlyStatusChangeEvent;
import org.jmock.Expectations;
import org.junit.After;
import org.jmock.Mockery;
import com.vaadin.data.Property.ReadOnlyException;
import com.vaadin.data.Property.ReadOnlyStatusChangeListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import java.lang.reflect.Method;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.github.peholmst.neo4jvaadindemo.article3.util.TestUtils.*;

/**
 * Test-case for {@link SimpleProperty}.
 * 
 * @author Petter Holmström
 */
public class SimplePropertyTest {

    Mockery context = new Mockery();
    ReadOnlyStatusChangeListener readOnlyListener;
    ValueChangeListener valueListener;

    static class TestClass implements java.io.Serializable {

        private String myValue;

        public String getMyValue() {
            return myValue;
        }

        public void setMyValue(String myValue) {
            this.myValue = myValue;
        }
    }
    Method myValueGetter;
    Method myValueSetter;
    TestClass item;

    @Before
    public void setUp() throws Exception {
        myValueGetter = TestClass.class.getMethod("getMyValue");
        myValueSetter = TestClass.class.getMethod("setMyValue", String.class);
        item = new TestClass();
        readOnlyListener = context.mock(ReadOnlyStatusChangeListener.class);
        valueListener = context.mock(ValueChangeListener.class);
    }

    @After
    public void tearDown() {
        context.assertIsSatisfied();
    }

    @Test
    public void readOnlyProperty_getValue() {
        SimpleProperty readOnly = SimpleProperty.readOnlyProperty(item, myValueGetter);
        item.setMyValue("Hello");
        assertEquals("Hello", readOnly.getValue());
    }

    @Test(expected = ReadOnlyException.class)
    public void readOnlyProperty_setValue() {
        SimpleProperty readOnly = SimpleProperty.readOnlyProperty(item, myValueGetter);
        readOnly.setValue("Hello");
    }

    @Test
    public void readOnlyProperty_getType() {
        SimpleProperty readOnly = SimpleProperty.readOnlyProperty(item, myValueGetter);
        assertSame(String.class, readOnly.getType());
    }

    @Test
    public void readOnlyProperty_isReadOnly() {
        SimpleProperty readOnly = SimpleProperty.readOnlyProperty(item, myValueGetter);
        assertTrue(readOnly.isReadOnly());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void readOnlyProperty_setReadOnly() {
        SimpleProperty readOnly = SimpleProperty.readOnlyProperty(item, myValueGetter);
        readOnly.setReadOnly(false);
    }

    @Test
    public void readOnlyProperty_nullItem_getValue() {
        SimpleProperty readOnly = SimpleProperty.readOnlyProperty(null, myValueGetter);
        assertNull(readOnly.getValue());
    }

    @Test
    public void writableProperty_setValue() {
        final SimpleProperty writable = SimpleProperty.writableProperty(item, myValueGetter, myValueSetter);

        context.checking(new Expectations() {

            {
                oneOf(valueListener).valueChange(with(any(ValueChangeEvent.class)));
            }
        });

        writable.addListener(valueListener);
        writable.setValue("Hello");
        assertEquals("Hello", item.getMyValue());
    }

    @Test
    public void writableProperty_isReadOnly() {
        SimpleProperty writable = SimpleProperty.writableProperty(item, myValueGetter, myValueSetter);
        assertFalse(writable.isReadOnly());
    }

    @Test
    public void writableProperty_setReadOnly() {
        final SimpleProperty writable = SimpleProperty.writableProperty(item, myValueGetter, myValueSetter);

        context.checking(new Expectations() {

            {
                oneOf(readOnlyListener).readOnlyStatusChange(with(any(ReadOnlyStatusChangeEvent.class)));
            }
        });

        writable.addListener(readOnlyListener);
        writable.setReadOnly(true);
        assertTrue(writable.isReadOnly());
    }

    @Test(expected = ReadOnlyException.class)
    public void writableProperty_readOnly_setValue() {
        SimpleProperty writable = SimpleProperty.writableProperty(item, myValueGetter, myValueSetter);
        writable.setReadOnly(true);
        writable.setValue("Hello");
    }
    
    @Test(expected = ReadOnlyException.class)
    public void writableProperty_nullItem_setValue() {
        SimpleProperty writable = SimpleProperty.writableProperty(null, myValueGetter, myValueSetter);
        writable.setValue("Hello");
    }
    
    @Test
    public void readOnlyPropertyWorksAfterSerialization() throws Exception {
        SimpleProperty readOnly = SimpleProperty.readOnlyProperty(item, myValueGetter);
        item.setMyValue("Hello");
        
        SimpleProperty deserialized = (SimpleProperty) deserialize(serialize(readOnly));
        assertEquals("Hello", deserialized.getValue());
        assertSame(String.class, deserialized.getType());
        assertTrue(deserialized.isReadOnly());        
    }

    @Test
    public void writablePropertyWorksAfterSerialization() throws Exception {
        SimpleProperty writable = SimpleProperty.writableProperty(item, myValueGetter, myValueSetter);
        
        SimpleProperty deserialized = (SimpleProperty) deserialize(serialize(writable));
        deserialized.setValue("Hello");
        assertFalse(deserialized.isReadOnly());        
    }
}
