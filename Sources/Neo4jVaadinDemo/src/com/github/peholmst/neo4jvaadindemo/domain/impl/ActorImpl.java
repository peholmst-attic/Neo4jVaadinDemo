package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;

import com.github.peholmst.neo4jvaadindemo.domain.Actor;

public class ActorImpl extends BaseAggregateRoot implements Actor {

	private static final long serialVersionUID = -2865191380528845109L;
	protected static final String KEY_NAME = "name";
	protected static final String KEY_DESCRIPTION = "description";

	public ActorImpl(Node wrappedNode, long id,
			GraphDatabaseServiceProvider serviceProvider) {
		super(wrappedNode, id, serviceProvider);
	}

	public ActorImpl(Node wrappedNode,
			GraphDatabaseServiceProvider serviceProvider) {
		super(wrappedNode, serviceProvider);
	}

	@Override
	public String getName() {
		return (String) getProperty(KEY_NAME);
	}

	@Override
	public void setName(String name) {
		setProperty(KEY_NAME, name);
	}

	@Override
	public String getDescription() {
		return (String) getProperty(KEY_DESCRIPTION);
	}

	@Override
	public void setDescription(String description) {
		setProperty(KEY_DESCRIPTION, description);
	}

	@Override
	protected void doCommit() {
		super.doCommit();
		// TODO Remove deleted actors!
		removedSuperActorNodeIds.clear();

		// Add new actors
		for (Actor addedActor : addedSuperActors) {
			final Node placeholderNode = getServiceProvider()
					.getGraphDatabaseService().createNode();
			placeholderNode.setProperty("referencedActorNodeId",
					((ActorImpl) addedActor).getNode().getId());
			getNode().createRelationshipTo(placeholderNode,
					RelationshipTypes.EXTENDS_ACTOR);
			placeholderNode.createRelationshipTo(
					((ActorImpl) addedActor).getNode(),
					RelationshipTypes.EXTENDS_ACTOR);
		}
		addedSuperActors.clear();
	}

	@Override
	public void discardChanges() {
		super.discardChanges();
		addedSuperActors.clear();
		removedSuperActorNodeIds.clear();
	}

	@Override
	public boolean hasUncommittedChanges() {
		return super.hasUncommittedChanges()
				|| !removedSuperActorNodeIds.isEmpty()
				|| !addedSuperActors.isEmpty();
	}

	private final Set<Long> removedSuperActorNodeIds = new HashSet<Long>();
	private final Collection<Actor> addedSuperActors = new LinkedList<Actor>();

	@Override
	public void addSuperActor(Actor actor) {
		// TODO Make sure that the actor has not already been added!
		long nodeId = ((ActorImpl) actor).getNode().getId();
		if (removedSuperActorNodeIds.contains(nodeId)) {
			removedSuperActorNodeIds.add(nodeId);
		} else {
			addedSuperActors.add(actor);
		}
	}

	@Override
	public void removeSuperActor(Actor actor) {
		if (addedSuperActors.contains(actor)) {
			addedSuperActors.remove(actor);
		} else {
			long nodeId = ((ActorImpl) actor).getNode().getId();
			removedSuperActorNodeIds.add(nodeId);
		}
	}

	protected Collection<Node> getSuperActorReferenceNodes() {
		Traverser traverser = getNode().traverse(Traverser.Order.BREADTH_FIRST,
				StopEvaluator.DEPTH_ONE,
				ReturnableEvaluator.ALL_BUT_START_NODE,
				RelationshipTypes.EXTENDS_ACTOR, Direction.OUTGOING);
		return traverser.getAllNodes();
	}

	protected Collection<Node> getSubActorReferenceNodes() {
		Traverser traverser = getNode().traverse(Traverser.Order.BREADTH_FIRST,
				StopEvaluator.DEPTH_ONE,
				ReturnableEvaluator.ALL_BUT_START_NODE,
				RelationshipTypes.EXTENDS_ACTOR, Direction.INCOMING);
		return traverser.getAllNodes();
	}

	@Override
	public Collection<Actor> getSuperActors() {
		Collection<Actor> superActorCollection = new LinkedList<Actor>();
		Collection<Node> referenceNodes = getSuperActorReferenceNodes();
		for (Node node : referenceNodes) {
			if (!removedSuperActorNodeIds.contains(node
					.getProperty("referencedActorNodeId"))) {
				superActorCollection.add(new ActorImpl(node
						.getSingleRelationship(RelationshipTypes.EXTENDS_ACTOR,
								Direction.OUTGOING).getEndNode(),
						getServiceProvider()));
			}
		}
		superActorCollection.addAll(addedSuperActors);
		return superActorCollection;
	}

	@Override
	public Collection<Actor> getSubActors() {
		// TODO Implement me!
		return null;
	}

}
