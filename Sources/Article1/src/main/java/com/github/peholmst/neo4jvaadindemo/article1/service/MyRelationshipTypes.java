package com.github.peholmst.neo4jvaadindemo.article1.service;

import org.neo4j.graphdb.RelationshipType;

/**
 * Enumeration containing the different types of relationships between nodes.
 * 
 * @author Petter Holmström
 */
public enum MyRelationshipTypes implements RelationshipType {

    /**
     * Relationship from the reference node to a node that references all the messages:
     * <code>(reference node) -- MESSAGES --> (messages node)</code>
     */
    MESSAGES,
    
    /**
     * Relationship from the messages node to a node that contains a message:
     * <code>
     * <pre>
     * (reference node) -- MESSAGES --> (tags node) -- CONTAINS_MESSAGE --> (message 1)
     *                                             |-- CONTAINS_MESSAGE --> (message 2)        
     * <pre>
     * </code>
     */
    CONTAINS_MESSAGE,
    
    /**
     * Relationship from the reference node to a node that references all tags:
     * <code>(reference node) -- TAGS --> (tags node)</code>
     */
    TAGS,
    
    /**
     * Relationship from the tags node to a node that contains a tag:
     * <code>
     * <pre>
     * (reference node) -- TAGS --> (tags node) -- CONTAINS_TAG --> (tag 1)
     *                                         |-- CONTAINS_TAG --> (tag 2)        
     * <pre>
     * </code>
     */
    CONTAINS_TAG,
    
    /**
     * Relationship from a message node to a tag node:
     * <code>
     * (message) -- IS_TAGGED_BY --> (tag)
     * </code>
     */
    IS_TAGGED_BY
}
