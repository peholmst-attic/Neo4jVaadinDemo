package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.Iterator;

import com.github.peholmst.neo4jvaadindemo.domain.Scope;
import com.github.peholmst.neo4jvaadindemo.domain.ScopeRepository;
import com.github.peholmst.neo4jvaadindemo.domain.impl.GraphDatabaseServiceProvider.TransactionJob;

public class ScopeRepositoryImpl extends BaseAggregateRootRepository<Scope>
		implements ScopeRepository {

	public ScopeRepositoryImpl(GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_SCOPES,
				RelationshipTypes.SR_SCOPE);
	}

	@Override
	public Scope create() {
		return (Scope) getServiceProvider().runInsideTransaction(
				new TransactionJob() {

					@Override
					public Object run() throws RuntimeException {
						return new ScopeImpl(createNode(), getNextId(),
								getServiceProvider());
					}
				}, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Scope> getAll() {
		return (Collection<Scope>) getServiceProvider().runInsideTransaction(
				new TransactionJob() {

					@Override
					public Object run() throws RuntimeException {
						return BaseNodeWrapper.wrapNodeCollection(
								getNodeCollection(), Scope.class,
								ScopeImpl.class, getServiceProvider());
					}
				}, true);
	}

	@Override
	public Iterator<Scope> getIterator() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

}
