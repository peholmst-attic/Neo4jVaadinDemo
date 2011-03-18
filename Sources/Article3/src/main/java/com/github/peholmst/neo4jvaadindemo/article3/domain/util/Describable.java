package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

public interface Describable extends java.io.Serializable {

    static final String PROP_DESCRIPTION = "description";
    
    String getDescription();
    
    void setDescription(String description);
}
