package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * The {@link GraphDatabaseService} interface is not serializable. The purpose
 * of this provider interface is to make it possible for serializable classes
 * to contain a serializable reference to the Neo4j graph database.
 * 
 * @author Petter Holmström
 */
public interface GraphDatabaseServiceProvider extends java.io.Serializable {

    /**
     * Returns the Neo4j graph database service.
     */
    GraphDatabaseService getGraphDb();
    
}
