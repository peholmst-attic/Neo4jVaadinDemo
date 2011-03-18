package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import com.github.peholmst.neo4jvaadindemo.article3.util.ItemProperty;

public interface Nameable extends java.io.Serializable {

    static final String PROP_NAME = "name";

    @ItemProperty
    String getName();
    
    void setName(String name);
}
