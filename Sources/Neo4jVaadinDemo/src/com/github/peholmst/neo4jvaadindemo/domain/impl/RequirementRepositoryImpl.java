package com.github.peholmst.neo4jvaadindemo.domain.impl;

import com.github.peholmst.neo4jvaadindemo.domain.Requirement;
import com.github.peholmst.neo4jvaadindemo.domain.RequirementRepository;

public class RequirementRepositoryImpl extends BaseAggregateRootRepository implements RequirementRepository {

	public RequirementRepositoryImpl(GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_REQUIREMENTS, RelationshipTypes.SR_REQUIREMENT);
	}

	@Override
	public Requirement createRequirement() {
		return new RequirementImpl(createNode(), getNextId(), getServiceProvider());
	}

}
