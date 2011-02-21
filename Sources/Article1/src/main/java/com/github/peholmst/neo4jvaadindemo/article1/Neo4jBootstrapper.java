package com.github.peholmst.neo4jvaadindemo.article1;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a servlet context listener that starts up an embedded neo4j database
 * upon initialization and shuts it down when destroyed. The directory of
 * the database files is specified as a system property named <code>neo4j.db.dir</code>.
 * 
 * @author Petter Holmström
 */
@WebListener
public class Neo4jBootstrapper implements ServletContextListener {

    private GraphDatabaseService graphDb;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Starting up Neo4j");
        /*
         * Neo4j needs a directory in which to store the database files.
         * First, see if one has been provided as a system property.
         */
        String neo4jdir = System.getProperty("neo4j.db.dir");

        if (neo4jdir == null) {
            // No directory specified, use a temporary directory
            throw new IllegalArgumentException("System property neo4j.db.dir was not specified");
        }
        log.info("Using database directory {} for Neo4j", neo4jdir);
        graphDb = new EmbeddedGraphDatabase(neo4jdir);
        // Finally, initialize the backend
        Backend.init(graphDb);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (graphDb != null) {
            log.info("Shutting down Neo4j");
            graphDb.shutdown();
        }
    }
}
