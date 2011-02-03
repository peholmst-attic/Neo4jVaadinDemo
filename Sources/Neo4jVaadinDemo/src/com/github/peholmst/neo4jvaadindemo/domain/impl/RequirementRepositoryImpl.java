package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.Iterator;

import com.github.peholmst.neo4jvaadindemo.domain.Requirement;
import com.github.peholmst.neo4jvaadindemo.domain.RequirementRepository;

public class RequirementRepositoryImpl extends BaseAggregateRootRepository<Requirement> implements RequirementRepository {

	public RequirementRepositoryImpl(GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_REQUIREMENTS, RelationshipTypes.SR_REQUIREMENT);
	}

	@Override
	public Requirement create() {
		return new RequirementImpl(createNode(), getNextId(), getServiceProvider());
	}

	@Override
	public Collection<Requirement> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Requirement> getIterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
