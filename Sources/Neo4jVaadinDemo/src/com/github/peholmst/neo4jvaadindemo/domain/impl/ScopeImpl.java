package com.github.peholmst.neo4jvaadindemo.domain.impl;

import org.neo4j.graphdb.Node;

import com.github.peholmst.neo4jvaadindemo.domain.Scope;

public class ScopeImpl extends BaseAggregateRoot implements Scope {

	private static final long serialVersionUID = 788493395522361437L;
	
	protected static final String KEY_NAME = "name";
	protected static final String KEY_DESCRIPTION = "description";

	public ScopeImpl(Node wrappedNode, long id,
			GraphDatabaseServiceProvider serviceProvider) {
		super(wrappedNode, id, serviceProvider);
	}

	public ScopeImpl(Node wrappedNode,
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
