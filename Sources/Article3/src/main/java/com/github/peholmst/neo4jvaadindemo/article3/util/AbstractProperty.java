package com.github.peholmst.neo4jvaadindemo.article3.util;

import com.github.peholmst.neo4jvaadindemo.article3.util.ListenerList.ListenerNotifier;
import com.vaadin.data.Property;

/**
 * Base class for {@link com.vaadin.data.Property} implementations. Provides
 * methods for firing {@link com.vaadin.data.Property.ValueChangeEvent}s and 
 * {@link com.vaadin.data.Property.ReadOnlyStatusChangeEvent}s
 * to registered listeners.
 * 
 * @author peholmst
 */
abstract class AbstractProperty implements Property, 
        Property.ReadOnlyStatusChangeNotifier, Property.ValueChangeNotifier {
    private final ListenerList<ReadOnlyStatusChangeListener> readOnlyListeners = new ListenerList<ReadOnlyStatusChangeListener>();
    private final ListenerList<ValueChangeListener> valueChangeListeners = new ListenerList<ValueChangeListener>();

    
    @Override
    public void addListener(ReadOnlyStatusChangeListener listener) {
        readOnlyListeners.add(listener);
    }

    @Override
    public void removeListener(ReadOnlyStatusChangeListener listener) {
        readOnlyListeners.remove(listener);
    }

    @Override
    public void addListener(ValueChangeListener listener) {
        valueChangeListeners.add(listener);
    }

    @Override
    public void removeListener(ValueChangeListener listener) {
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
