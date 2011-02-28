package com.github.peholmst.neo4jvaadindemo.article2;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.impl.transaction.LockManager;

/**
 * This is a singleton class for accessing the Neo4j Graph Database Service
 * once it has been initialized by the {@link Neo4jBootstrapper}.
 *
 * @author Petter Holmström
 */
public class Backend {

    private static Backend INSTANCE;

    /**
     * Initializes the singleton with the specified graph database service and lock manager instances.
     * @param graphDb the graph database service instance.
     * @param lockManager the lock manager instance.
     */
    static void init(GraphDatabaseService graphDb, LockManager lockManager) {
        INSTANCE = new Backend(graphDb, lockManager);
    }

    /**
     * Gets the singleton instance.
     */
    public static Backend getInstance() {
        return INSTANCE;
    }
    private final GraphDatabaseService graphDb;

    private final LockManager lockManager;
    
    private Backend(GraphDatabaseService graphDb, LockManager lockManager) {
        this.graphDb = graphDb;
        this.lockManager = lockManager;
    }

    /**
     * Gets the graph database service.
     * @return the graph database service.
     */
    public GraphDatabaseService getGraphDb() {
        return graphDb;
    }

    /**
     * Gets the lock manager.
     * @return the lock manager.
     */
    public LockManager getLockManager() {
        return lockManager;
    }    
    
}
