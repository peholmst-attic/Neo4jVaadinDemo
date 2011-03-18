package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import com.github.peholmst.neo4jvaadindemo.article3.util.ItemProperty;
import java.util.UUID;

public interface Entity extends java.io.Serializable {

    static final String PROP_IDENTIFIER = "identifier";
    
    @ItemProperty
    UUID getIdentifier();
    
    public boolean isInvalid();
    
}
