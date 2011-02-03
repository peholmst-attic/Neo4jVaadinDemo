package com.github.peholmst.neo4jvaadindemo.testing;

import java.io.File;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public abstract class BaseDatabaseTest {

	protected GraphDatabaseService createDb() {
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		if (!tempDir.canWrite()) {
			throw new IllegalStateException(
					"Cannot write to temporary directory " + tempDir);
		}
		String directory = tempDir.getAbsolutePath() + File.separator
				+ "neo4jvaadindemo" + System.currentTimeMillis() + "_"
				+ new Random().nextLong();
		System.out.println("Creating test database in directory " + directory);

		GraphDatabaseService graphDb = new EmbeddedGraphDatabase(directory);
		return graphDb;
	}

	private GraphDatabaseService graphDb;

	@Before
	public void setUp() {
		graphDb = createDb();
		Transaction tx = getGraphDb().beginTx();
		try {
			setUpInsideTransaction();
			tx.success();
		} finally {
			tx.finish();
		}
	}

	protected void setUpInsideTransaction() {

	}

	protected void tearDownInsideTransaction() {

	}

	@After
	public void tearDown() {
		Transaction tx = getGraphDb().beginTx();
		try {
			tearDownInsideTransaction();
			tx.success();
		} finally {
			tx.finish();
		}
		System.out.println("Shutting down test database");
		graphDb.shutdown();
	}

	protected GraphDatabaseService getGraphDb() {
		return graphDb;
	}
}
