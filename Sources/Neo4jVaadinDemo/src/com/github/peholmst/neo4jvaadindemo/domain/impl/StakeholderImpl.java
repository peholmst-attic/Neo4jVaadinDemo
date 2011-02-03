package com.github.peholmst.neo4jvaadindemo.domain.impl;

import org.neo4j.graphdb.Node;

import com.github.peholmst.neo4jvaadindemo.domain.Stakeholder;

public class StakeholderImpl extends BaseAggregateRoot implements Stakeholder {

	private static final long serialVersionUID = 8795938576157100215L;
	
	protected static final String KEY_NAME = "name";
	protected static final String KEY_DESCRIPTION = "description";

	public StakeholderImpl(Node wrappedNode, long id, GraphDatabaseServiceProvider serviceProvider) {
		super(wrappedNode, id, serviceProvider);
	}

	public StakeholderImpl(Node wrappedNode, GraphDatabaseServiceProvider serviceProvider) {
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
