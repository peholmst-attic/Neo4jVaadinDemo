package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.logging.Logger;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;

import com.github.peholmst.neo4jvaadindemo.domain.impl.GraphDatabaseServiceProvider.TransactionJob;

public abstract class BaseRepository {

	private final Node factoryNode;
	private final RelationshipType subrefToNodesType;
	protected final Logger logger = Logger.getLogger(getClass().getName());
	private final GraphDatabaseServiceProvider serviceProvider;

	public BaseRepository(final GraphDatabaseServiceProvider serviceProvider,
			final RelationshipType startToSubrefType,
			final RelationshipType subrefToNodesType) {
		if (serviceProvider == null) {
			throw new IllegalArgumentException("null serviceProvider");
		}
		if (startToSubrefType == null) {
			throw new IllegalArgumentException("null startToSubrefType");
		}
		if (subrefToNodesType == null) {
			throw new IllegalArgumentException("null subrefToNodesType");
		}
		this.serviceProvider = serviceProvider;
		factoryNode = (Node) serviceProvider.runInsideTransaction(
				new TransactionJob() {

					@Override
					public Object run() throws RuntimeException {
						Relationship rel = getGraphDb().getReferenceNode()
								.getSingleRelationship(startToSubrefType,
										Direction.OUTGOING);
						if (rel == null) {
							Node factoryNode = getGraphDb().createNode();
							getGraphDb().getReferenceNode()
									.createRelationshipTo(factoryNode,
											startToSubrefType);
							return factoryNode;
						} else {
							return rel.getEndNode();
						}
					}
				}, false);
		this.subrefToNodesType = subrefToNodesType;
	}

	protected final Node getFactoryNode() {
		return factoryNode;
	}

	protected final GraphDatabaseService getGraphDb() {
		return getServiceProvider().getGraphDatabaseService();
	}

	protected final GraphDatabaseServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	protected Node createNode() {
		logger.info("Creating new node");
		Node node = getGraphDb().createNode();
		factoryNode.createRelationshipTo(node, subrefToNodesType);
		return node;
	}

	protected Collection<Node> getNodeCollection() {
		logger.info("Getting all nodes");
		Traverser traverser = factoryNode.traverse(
				Traverser.Order.BREADTH_FIRST, StopEvaluator.DEPTH_ONE,
				ReturnableEvaluator.ALL_BUT_START_NODE, subrefToNodesType,
				Direction.OUTGOING);
		return traverser.getAllNodes();
	}

}
