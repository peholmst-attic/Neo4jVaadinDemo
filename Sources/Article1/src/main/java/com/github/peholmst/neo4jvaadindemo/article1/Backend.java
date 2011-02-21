package com.github.peholmst.neo4jvaadindemo.article1;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * This is a singleton class for accessing the Neo4j Graph Database Service
 * once it has been initialized by the {@link Neo4jBootstrapper}.
 *
 * @author Petter Holmström
 */
public class Backend {

    private static Backend INSTANCE;

    /**
     * Initializes the singleton with the specified graph database service instance.
     * @param graphDb the graph database service instance.
     */
    static void init(GraphDatabaseService graphDb) {
        INSTANCE = new Backend(graphDb);
    }

    /**
     * Gets the singleton instance.
     */
    public static Backend getInstance() {
        return INSTANCE;
    }
    private final GraphDatabaseService graphDb;

    private Backend(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    /**
     * Gets the graph database service.
     * @return the graph database service.
     */
    public GraphDatabaseService getGraphDb() {
        return graphDb;
    }
}
