package com.github.peholmst.neo4jvaadindemo;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;

@WebListener
public class Neo4jBootstrapper implements ServletContextListener {

	private final Logger logger = Logger.getLogger(getClass().getName());
	
	private GraphDatabaseService graphDb;
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		logger.info("Shutting down Neo4j");
		if (graphDb != null) {
			graphDb.shutdown();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		logger.info("Starting up Neo4j");
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		if (!tempDir.canWrite()) {
			logger.log(Level.SEVERE, "Cannot write to temporary directory {0}", tempDir);
			throw new IllegalStateException(
					"Cannot write to temporary directory " + tempDir);
		}
		String directory = tempDir.getAbsolutePath() + File.separator
				+ "neo4jvaadindemo.db";
		logger.log(Level.INFO, "Using database directory {0}", directory);
		graphDb = new EmbeddedGraphDatabase(directory);
		logger.info("Initializing Backend");
		Backend.init(graphDb);
	}

}
