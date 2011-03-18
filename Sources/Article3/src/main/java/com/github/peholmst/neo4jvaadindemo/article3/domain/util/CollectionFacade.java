package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import com.vaadin.data.Container;
import java.util.Collection;
import java.util.Iterator;

public interface CollectionFacade<T> extends java.io.Serializable {

    Collection<T> asCollection();
    
    Iterator<T> asIterator();
    
    Container asContainer();
}
