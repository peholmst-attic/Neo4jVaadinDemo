package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

/**
 * Base interface for entities that act as aggregate roots.
 * @author Petter Holmström
 */
public interface AggregateRoot extends Entity {

    /**
     * Commits all the changes to the underlying Neo4j graph.
     */
    void commitChanges();

    /**
     * Discards all the changes.
     */
    void discardChanges();

    /**
     * Checks if there are uncommitted changes.
     */
    boolean hasUncommittedChanges();

    /**
     * Deletes the aggregate root and all its aggregates.
     */
    void delete();
}
