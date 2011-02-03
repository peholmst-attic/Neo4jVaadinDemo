package com.github.peholmst.neo4jvaadindemo.domain.impl;

import org.neo4j.graphdb.GraphDatabaseService;

public interface GraphDatabaseServiceProvider extends java.io.Serializable {

	GraphDatabaseService getGraphDatabaseService();

	interface TransactionJob {

		Object run() throws RuntimeException;
	}

	/**
	 * Runs <code>job</code> inside a transaction. If the transaction is not
	 * read only, it will be committed when the job is finished unless any
	 * exceptions occur, in which case the transaction will be rolled back.
	 * 
	 * @param job
	 *            the job to run, must not be <code>null</code>.
	 * @param readOnly
	 *            true to run inside a read only transaction (i.e. there won't
	 *            be any changes to commit), false to run inside a read-write
	 *            transaction.
	 * @return the object returned by {@link TransactionJob#run()}.
	 * @throws RuntimeException
	 *             any exceptions thrown by {@link TransactionJob#run()} or
	 *             Neo4j.
	 */
	Object runInsideTransaction(TransactionJob job, boolean readOnly)
			throws RuntimeException;

}
