package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.Iterator;

import org.neo4j.graphdb.Node;

import com.github.peholmst.neo4jvaadindemo.domain.Actor;
import com.github.peholmst.neo4jvaadindemo.domain.ActorRepository;

public class ActorRepositoryImpl extends BaseAggregateRootRepository<Actor>
		implements ActorRepository {

	public ActorRepositoryImpl(GraphDatabaseServiceProvider serviceProvider) {
		super(serviceProvider, RelationshipTypes.SR_ACTORS,
				RelationshipTypes.SR_ACTOR);
	}

	@Override
	public Actor doCreate() {
		return new ActorImpl(createNode(), getNextId(), getServiceProvider());
	}

	@Override
	public Collection<Actor> getAll() {
		return BaseNodeWrapper.wrapNodeCollection(getNodeCollection(),
				Actor.class, ActorImpl.class, getServiceProvider());
	}

	@Override
	public Iterator<Actor> getIterator() {
		return new BaseNodeWrapperIterator<Actor>(getFactoryNode(),
				RelationshipTypes.SR_ACTOR) {
			@Override
			protected Actor wrapNode(Node node) {
				return new ActorImpl(node, getServiceProvider());
			}
		};
	}

}
