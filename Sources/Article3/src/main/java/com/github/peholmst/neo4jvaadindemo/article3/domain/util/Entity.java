package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import java.util.UUID;

public interface Entity extends java.io.Serializable {

    static final String PROP_IDENTIFIER = "identifier";
    
    UUID getIdentifier();
}
