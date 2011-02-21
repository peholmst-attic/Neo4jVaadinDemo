package com.github.peholmst.neo4jvaadindemo.article1.ui;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A very simple Vaadin container implementation that wraps a collection of Strings.
 * No editing is allowed.
 * 
 * @author Petter Holmström
 */
public class StringContainer implements Container, Container.Indexed, Container.Ordered {
   
    private final List<String> strings;
    
    private final String propertyId;
    
    /**
     * Creates a new <code>StringContainer</code>.
     * @param strings the collection of strings to show.
     * @param propertyId the property ID that will be used for the string value.
     */
    public StringContainer(Collection<String> strings, String propertyId) {
        this.strings = new ArrayList<String>(strings);
        this.propertyId = propertyId;
    }
    
    @Override
    public Item getItem(Object itemId) {
        return new StringItem((String) itemId);
    }

    @Override
    public Collection<?> getContainerPropertyIds() {
        return Collections.unmodifiableList(Arrays.asList(propertyId));
    }

    @Override
    public Collection<?> getItemIds() {
        return Collections.unmodifiableCollection(strings);
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        if (this.propertyId.equals(propertyId)) {
            return new StringProperty((String) itemId);
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getType(Object propertyId) {
        if (this.propertyId.equals(propertyId)) {
            return String.class;
        } else {
            return null;
        }
    }

    @Override
    public int size() {
        return strings.size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return strings.contains((String) itemId);
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOfId(Object itemId) {
        return strings.indexOf(itemId);
    }

    @Override
    public Object getIdByIndex(int index) {
        return strings.get(index);
    }

    @Override
    public Object addItemAt(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object nextItemId(Object itemId) {
        int index = indexOfId(itemId);
        if (index > -1 && index < size() -1) {
            return getIdByIndex(index + 1);
        } else {
            return null;
        }
    }

    @Override
    public Object prevItemId(Object itemId) {
        int index = indexOfId(itemId);
        if (index > 0) {
            return getIdByIndex(index - 1);
        } else {
            return null;
        }
    }

    @Override
    public Object firstItemId() {
        return strings.isEmpty() ? null : strings.get(0);
    }

    @Override
    public Object lastItemId() {
        return strings.isEmpty() ? null : strings.get(strings.size() -1);
    }

    @Override
    public boolean isFirstId(Object itemId) {
        return itemId.equals(firstItemId());
    }

    @Override
    public boolean isLastId(Object itemId) {
        return itemId.equals(lastItemId());
    }

    @Override
    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
    
    private class StringItem implements Item {

        private final String value;
        
        public StringItem(String value) {
            this.value = value;
        }
        
        @Override
        public Property getItemProperty(Object id) {
            return getContainerProperty(value, id);
        }

        @Override
        public Collection<?> getItemPropertyIds() {
            return getContainerPropertyIds();
        }

        @Override
        public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }
        
    }
    
    private class StringProperty implements Property {

        private final String value;
        
        public StringProperty(String value) {
            this.value = value;
        }
        
        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }             

        @Override
        public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
            throw new ReadOnlyException();
        }

        @Override
        public Class<?> getType() {
            return String.class;
        }

        @Override
        public boolean isReadOnly() {
            return true;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            throw new UnsupportedOperationException();
        }        
    }
}
