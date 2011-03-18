package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import com.github.peholmst.neo4jvaadindemo.article3.util.ItemProperty;

public interface Describable extends java.io.Serializable {

    static final String PROP_DESCRIPTION = "description";
    
    @ItemProperty
    String getDescription();
    
    void setDescription(String description);
}
