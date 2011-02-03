package com.github.peholmst.neo4jvaadindemo.domain.impl;

import com.github.peholmst.neo4jvaadindemo.domain.Scope;
import com.github.peholmst.neo4jvaadindemo.domain.ScopeRepository;

public class ScopeRepositoryImpl extends BaseAggregateRootRepository implements ScopeRepository {

	public ScopeRepositoryImpl(GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_SCOPES, RelationshipTypes.SR_SCOPE);
	}

	@Override
	public Scope createScope() {
		return new ScopeImpl(createNode(), getNextId(), getServiceProvider());
	}

}
