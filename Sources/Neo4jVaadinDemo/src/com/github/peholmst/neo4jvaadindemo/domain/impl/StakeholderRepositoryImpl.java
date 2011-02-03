package com.github.peholmst.neo4jvaadindemo.domain.impl;

import com.github.peholmst.neo4jvaadindemo.domain.Stakeholder;
import com.github.peholmst.neo4jvaadindemo.domain.StakeholderRepository;

public class StakeholderRepositoryImpl extends BaseAggregateRootRepository implements
		StakeholderRepository {

	public StakeholderRepositoryImpl(GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_STAKEHOLDERS,
				RelationshipTypes.SR_STAKEHOLDER);
	}

	@Override
	public Stakeholder createStakeholder() {
		return new StakeholderImpl(createNode(), getNextId(), getServiceProvider());
	}

}
