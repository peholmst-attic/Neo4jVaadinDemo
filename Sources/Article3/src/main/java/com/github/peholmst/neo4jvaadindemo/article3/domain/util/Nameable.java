package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import com.github.peholmst.neo4jvaadindemo.article3.util.ItemProperty;

/**
 * Interface to be implemented by any entity that has a <code>name</code> property.
 * @author Petter Holmström
 */
public interface Nameable extends java.io.Serializable {

    String PROP_NAME = "name";

    @ItemProperty
    String getName();
    
    void setName(String name);
}
