package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.Iterator;

import com.github.peholmst.neo4jvaadindemo.domain.Scope;
import com.github.peholmst.neo4jvaadindemo.domain.ScopeRepository;

public class ScopeRepositoryImpl extends BaseAggregateRootRepository<Scope> implements ScopeRepository {

	public ScopeRepositoryImpl(GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_SCOPES, RelationshipTypes.SR_SCOPE);
	}

	@Override
	public Scope create() {
		return new ScopeImpl(createNode(), getNextId(), getServiceProvider());
	}

	@Override
	public Collection<Scope> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Scope> getIterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
