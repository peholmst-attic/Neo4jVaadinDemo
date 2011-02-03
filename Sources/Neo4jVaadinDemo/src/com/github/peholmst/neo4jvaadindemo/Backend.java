package com.github.peholmst.neo4jvaadindemo;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import com.github.peholmst.neo4jvaadindemo.domain.ActorRepository;
import com.github.peholmst.neo4jvaadindemo.domain.RequirementRepository;
import com.github.peholmst.neo4jvaadindemo.domain.ScopeRepository;
import com.github.peholmst.neo4jvaadindemo.domain.StakeholderRepository;
import com.github.peholmst.neo4jvaadindemo.domain.UseCaseRepository;
import com.github.peholmst.neo4jvaadindemo.domain.impl.ActorRepositoryImpl;
import com.github.peholmst.neo4jvaadindemo.domain.impl.GraphDatabaseServiceProvider;
import com.github.peholmst.neo4jvaadindemo.domain.impl.RequirementRepositoryImpl;
import com.github.peholmst.neo4jvaadindemo.domain.impl.ScopeRepositoryImpl;
import com.github.peholmst.neo4jvaadindemo.domain.impl.StakeholderRepositoryImpl;

public class Backend {

	private static Backend INSTANCE;

	static void init(GraphDatabaseService graphDb) {
		new Backend(graphDb);
	}

	private static class ServiceProvider implements
			GraphDatabaseServiceProvider {

		private static final long serialVersionUID = 452570196475430074L;

		@Override
		public GraphDatabaseService getGraphDatabaseService() {
			return Backend.getInstance().getGraphDb();
		}

		@Override
		public Object runInsideTransaction(TransactionJob job, boolean readOnly)
				throws RuntimeException {
			return Backend.getInstance().runInsideTransaction(job, readOnly);
		}

	}

	private final GraphDatabaseServiceProvider graphDbProvider = new ServiceProvider();

	private final GraphDatabaseService graphDb;

	private final ActorRepositoryImpl actorRepository;

	private final StakeholderRepositoryImpl stakeholderRepository;

	private final RequirementRepositoryImpl requirementRepository;

	private final ScopeRepositoryImpl scopeRepository;

	private Backend(GraphDatabaseService graphDb) {
		INSTANCE = this;
		this.graphDb = graphDb;
		Transaction tx = graphDb.beginTx();
		try {
			actorRepository = new ActorRepositoryImpl(graphDbProvider);
			stakeholderRepository = new StakeholderRepositoryImpl(
					graphDbProvider);
			requirementRepository = new RequirementRepositoryImpl(
					graphDbProvider);
			scopeRepository = new ScopeRepositoryImpl(graphDbProvider);
			tx.success();
		} finally {
			tx.finish();
		}
	}

	public static Backend getInstance() {
		return INSTANCE;
	}

	public ActorRepository getActorRepository() {
		return actorRepository;
	}

	public RequirementRepository getRequirementRepository() {
		return requirementRepository;
	}

	public ScopeRepository getScopeRepository() {
		return scopeRepository;
	}

	public StakeholderRepository getStakeholderRepository() {
		return stakeholderRepository;
	}

	public UseCaseRepository getUseCaseRepository() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	private GraphDatabaseService getGraphDb() {
		return graphDb;
	}
	
	private final ThreadLocal<Transaction> currentTransaction = new ThreadLocal<Transaction>() {
		@Override
		public void remove() {
			Transaction tx = get();
			if (tx != null) {
				tx.finish();
			}
			super.remove();
		}
	};
	
	private Object runInsideTransaction(
			GraphDatabaseServiceProvider.TransactionJob job, boolean readOnly)
			throws RuntimeException {
		Transaction tx = currentTransaction.get();
		if (tx == null) {
			tx = getGraphDb().beginTx();
			currentTransaction.set(tx);
		}
		try {
			Object result = job.run();
			if (!readOnly) {
				tx.success();
				currentTransaction.remove();
			}
			return result;
		} catch (RuntimeException e) {
			tx.failure();
			currentTransaction.remove();
			throw e;
		}
	}
}
