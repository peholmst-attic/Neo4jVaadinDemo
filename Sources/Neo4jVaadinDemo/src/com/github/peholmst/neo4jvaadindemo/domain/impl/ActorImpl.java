package com.github.peholmst.neo4jvaadindemo.domain.impl;

import org.neo4j.graphdb.Node;

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

}
