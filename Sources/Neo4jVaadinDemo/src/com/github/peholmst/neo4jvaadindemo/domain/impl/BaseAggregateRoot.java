package com.github.peholmst.neo4jvaadindemo.domain.impl;

import org.neo4j.graphdb.Node;

import com.github.peholmst.neo4jvaadindemo.domain.AggregateRoot;

public abstract class BaseAggregateRoot extends BaseNodeWrapper implements AggregateRoot {

	private static final long serialVersionUID = -5389862630284882588L;
	
	protected static final String KEY_ID = "id";
	
	public BaseAggregateRoot(Node wrappedNode, long id, GraphDatabaseServiceProvider serviceProvider) {
		super(wrappedNode, serviceProvider);
		getNode().setProperty(KEY_ID, new Long(id));
	}
	
	public BaseAggregateRoot(Node wrappedNode, GraphDatabaseServiceProvider serviceProvider) {
		super(wrappedNode, serviceProvider);
	}

	@Override
	public long getIdentifier() {
		return (Long) getProperty(KEY_ID);
	}
	
}
