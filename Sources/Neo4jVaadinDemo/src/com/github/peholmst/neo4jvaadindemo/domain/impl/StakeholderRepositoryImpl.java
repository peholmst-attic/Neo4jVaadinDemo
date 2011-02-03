package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.Iterator;

import com.github.peholmst.neo4jvaadindemo.domain.Stakeholder;
import com.github.peholmst.neo4jvaadindemo.domain.StakeholderRepository;

public class StakeholderRepositoryImpl extends BaseAggregateRootRepository<Stakeholder> implements
		StakeholderRepository {

	public StakeholderRepositoryImpl(GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_STAKEHOLDERS,
				RelationshipTypes.SR_STAKEHOLDER);
	}

	@Override
	public Stakeholder create() {
		return new StakeholderImpl(createNode(), getNextId(), getServiceProvider());
	}

	@Override
	public Collection<Stakeholder> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Stakeholder> getIterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
