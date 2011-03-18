package com.github.peholmst.neo4jvaadindemo.article3.util;

import java.util.LinkedList;

public class ListenerList<T> implements java.io.Serializable {

    private final LinkedList<T> listeners = new LinkedList<T>();
    
    public void add(T listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }
    
    public void remove(T listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
    
    public interface ListenerNotifier<T> {
        void notifyListener(T listener);
    }
    
    public void notifyListeners(ListenerNotifier<T> notifier) {
        LinkedList<T> listenersToNotify = (LinkedList<T>) listeners.clone();
        for (T listener : listenersToNotify) {
            notifier.notifyListener(listener);
        }
    }
}
