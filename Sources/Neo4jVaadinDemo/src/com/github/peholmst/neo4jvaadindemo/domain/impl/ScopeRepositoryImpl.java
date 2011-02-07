package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.Iterator;

import com.github.peholmst.neo4jvaadindemo.domain.Scope;
import com.github.peholmst.neo4jvaadindemo.domain.ScopeRepository;

public class ScopeRepositoryImpl extends BaseAggregateRootRepository<Scope>
		implements ScopeRepository {

	public ScopeRepositoryImpl(GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_SCOPES,
				RelationshipTypes.SR_SCOPE);
	}

	@Override
	public Scope doCreate() {
		return new ScopeImpl(createNode(), getNextId(), getServiceProvider());
	}

	@Override
	public Collection<Scope> getAll() {
		return BaseNodeWrapper.wrapNodeCollection(getNodeCollection(),
				Scope.class, ScopeImpl.class, getServiceProvider());
	}

	@Override
	public Iterator<Scope> getIterator() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

}
