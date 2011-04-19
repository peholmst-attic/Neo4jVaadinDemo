package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import com.vaadin.data.Container;
import java.util.Collection;
import java.util.Iterator;

/**
 * This interface defines a facade to a collection of items. The items can be accessed
 * through an iterator, as a Java {@link Collection} or as a Vaadin {@link Container}.
 * 
 * @author Petter Holmström
 */
public interface CollectionFacade<T> extends java.io.Serializable {

    /**
     * Returns the collection as a Java collection. The collection is unmodifiable.
     */
    Collection<T> asCollection();

    /**
     * Returns an iterator over the collection. {@link Iterator#remove() } is not supported.
     */
    Iterator<T> asIterator();

    /**
     * Returns the collection as a Vaadin container. Items cannot be added to nor removed from the container.
     */
    Container asContainer();
}
