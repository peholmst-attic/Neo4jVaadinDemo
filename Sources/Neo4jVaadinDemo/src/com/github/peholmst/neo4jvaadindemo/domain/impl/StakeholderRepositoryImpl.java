package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.Iterator;

import com.github.peholmst.neo4jvaadindemo.domain.Stakeholder;
import com.github.peholmst.neo4jvaadindemo.domain.StakeholderRepository;
import com.github.peholmst.neo4jvaadindemo.domain.impl.GraphDatabaseServiceProvider.TransactionJob;

public class StakeholderRepositoryImpl extends
		BaseAggregateRootRepository<Stakeholder> implements
		StakeholderRepository {

	public StakeholderRepositoryImpl(
			GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_STAKEHOLDERS,
				RelationshipTypes.SR_STAKEHOLDER);
	}

	@Override
	public Stakeholder create() {
		return (Stakeholder) getServiceProvider().runInsideTransaction(
				new TransactionJob() {

					@Override
					public Object run() throws RuntimeException {
						return new StakeholderImpl(createNode(), getNextId(),
								getServiceProvider());
					}
				}, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Stakeholder> getAll() {
		return (Collection<Stakeholder>) getServiceProvider()
				.runInsideTransaction(new TransactionJob() {

					@Override
					public Object run() throws RuntimeException {
						return BaseNodeWrapper.wrapNodeCollection(
								getNodeCollection(), Stakeholder.class,
								StakeholderImpl.class, getServiceProvider());
					}
				}, true);
	}

	@Override
	public Iterator<Stakeholder> getIterator() {
		throw new UnsupportedOperationException("Not implemented yet!");
	}

}
