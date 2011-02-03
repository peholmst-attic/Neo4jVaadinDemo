package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Iterator;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;

public abstract class BaseNodeWrapperIterator<T> implements Iterator<T> {

	private final Iterator<Node> nodeIterator;

	public BaseNodeWrapperIterator(Node startNode,
			RelationshipType relationshipType) {
		nodeIterator = startNode.traverse(Traverser.Order.BREADTH_FIRST,
				StopEvaluator.DEPTH_ONE,
				ReturnableEvaluator.ALL_BUT_START_NODE, relationshipType,
				Direction.OUTGOING).iterator();
	}

	@Override
	public boolean hasNext() {
		return nodeIterator.hasNext();
	}

	@Override
	public T next() {
		Node nextNode = nodeIterator.next();
		return wrapNode(nextNode);
	}

	protected abstract T wrapNode(Node node);

	@Override
	public void remove() {
		nodeIterator.remove();
	}
}
