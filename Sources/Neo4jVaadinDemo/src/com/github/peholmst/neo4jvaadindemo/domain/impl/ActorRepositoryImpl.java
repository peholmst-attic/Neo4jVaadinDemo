package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.Iterator;

import org.neo4j.graphdb.Node;

import com.github.peholmst.neo4jvaadindemo.domain.Actor;
import com.github.peholmst.neo4jvaadindemo.domain.ActorRepository;
import com.github.peholmst.neo4jvaadindemo.domain.impl.GraphDatabaseServiceProvider.TransactionJob;

public class ActorRepositoryImpl extends BaseAggregateRootRepository implements
		ActorRepository {

	public ActorRepositoryImpl(GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_ACTORS,
				RelationshipTypes.SR_ACTOR);
	}

	@Override
	public Actor createActor() {
		return (Actor) getServiceProvider().runInsideTransaction(
				new TransactionJob() {

					@Override
					public Object run() throws RuntimeException {
						return new ActorImpl(createNode(), getNextId(),
								getServiceProvider());
					}
				}, false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Actor> getActors() {
		return (Collection<Actor>) getServiceProvider().runInsideTransaction(
				new TransactionJob() {

					@Override
					public Object run() throws RuntimeException {
						return BaseNodeWrapper.wrapNodeCollection(
								getNodeCollection(), Actor.class,
								ActorImpl.class, getServiceProvider());
					}
				}, true);
	}

	@Override
	public Iterator<Actor> getActorIterator() {
		// TODO Figure out how to handle transactions!
		return new BaseNodeWrapperIterator<Actor>(getFactoryNode(),
				RelationshipTypes.SR_ACTOR) {
			@Override
			protected Actor wrapNode(Node node) {
				return new ActorImpl(node, getServiceProvider());
			}
		};
	}

}
