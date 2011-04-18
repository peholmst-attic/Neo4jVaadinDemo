package com.github.peholmst.neo4jvaadindemo.article3.util;

import java.util.LinkedList;

/**
 * A list of listeners that can be notified using the Visitor pattern.
 * 
 * @author Petter Holmström
 */
public class ListenerList<T> implements java.io.Serializable {

    private final LinkedList<T> listeners = new LinkedList<T>();
    
    /**
     * Adds the specified listener to the list. If the listener is <code>null</code>, nothing happens.
     */
    public void add(T listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    /**
     * Removes the specified listener to the list. If the listener is <code>null</code>, or is not
     * in the list, nothing happens.
     */
    public void remove(T listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
    
    /**
     * Visitor interface (as in the Visitor pattern) for notifying the listeners in the list.
     */
    public interface ListenerNotifier<T> {
        void notifyListener(T listener);
    }
    
    /**
     * Visits (as in the Visitor pattern) all listeners in the list
     * with the specified notifier (cannot be <code>null</code>).
     */
    public void notifyListeners(ListenerNotifier<T> notifier) {
        assert notifier != null : "notifier must not be null";
        LinkedList<T> listenersToNotify = (LinkedList<T>) listeners.clone();
        for (T listener : listenersToNotify) {
            notifier.notifyListener(listener);
        }
    }
}
