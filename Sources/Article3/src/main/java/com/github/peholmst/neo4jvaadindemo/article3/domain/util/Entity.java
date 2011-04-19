package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import com.github.peholmst.neo4jvaadindemo.article3.util.ItemProperty;
import java.util.UUID;

/**
 * Base interface for all entities.
 * @author Petter Holmström
 */
public interface Entity extends java.io.Serializable {

    String PROP_IDENTIFIER = "identifier";
    
    /**
     * Returns the unique identifier for this entity.
     */
    @ItemProperty
    UUID getIdentifier();
    
    /**
     * Checks if the Neo4j nodes that this entity interface wraps are valid. An entity
     * can become invalid if changes are made to the underlying nodes by another client,
     * e.g. the wrapped node is deleted.
     */
    public boolean isInvalid();
    
}
