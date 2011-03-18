package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

public interface Nameable extends java.io.Serializable {

    static final String PROP_NAME = "name";
    
    String getName();
    
    void setName(String name);
}
