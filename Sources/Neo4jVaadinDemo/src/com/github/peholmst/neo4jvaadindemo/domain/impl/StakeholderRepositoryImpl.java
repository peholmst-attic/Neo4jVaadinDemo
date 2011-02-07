package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.Iterator;

import com.github.peholmst.neo4jvaadindemo.domain.Stakeholder;
import com.github.peholmst.neo4jvaadindemo.domain.StakeholderRepository;

public class StakeholderRepositoryImpl extends
		BaseAggregateRootRepository<Stakeholder> implements
		StakeholderRepository {

	public StakeholderRepositoryImpl(
			GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_STAKEHOLDERS,
				RelationshipTypes.SR_STAKEHOLDER);
	}

	@Override
	public Stakeholder doCreate() {
		return new StakeholderImpl(createNode(), getNextId(),
				getServiceProvider());
	}

	@Override
	public Collection<Stakeholder> getAll() {
		return BaseNodeWrapper.wrapNodeCollection(getNodeCollection(),
				Stakeholder.class, StakeholderImpl.class, getServiceProvider());
	}

	@Override
	public Iterator<Stakeholder> getIterator() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

}
