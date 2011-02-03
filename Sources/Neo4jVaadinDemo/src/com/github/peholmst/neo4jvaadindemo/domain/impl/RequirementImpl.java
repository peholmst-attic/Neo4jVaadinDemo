package com.github.peholmst.neo4jvaadindemo.domain.impl;

import org.neo4j.graphdb.Node;

import com.github.peholmst.neo4jvaadindemo.domain.Requirement;

public class RequirementImpl extends BaseAggregateRoot implements Requirement {

	private static final long serialVersionUID = -6565798954609199564L;

	protected static final String KEY_NAME = "name";
	protected static final String KEY_DESCRIPTION = "description";
	protected static final String KEY_TYPE = "type";

	public RequirementImpl(Node wrappedNode, long id,
			GraphDatabaseServiceProvider serviceProvider) {
		super(wrappedNode, id, serviceProvider);
	}

	public RequirementImpl(Node wrappedNode,
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
	public Type getType() {
		String value = (String) getProperty(KEY_TYPE);
		if (value == null) {
			return null;
		} else {
			return Type.valueOf(value);
		}
	}

	@Override
	public void setType(Type type) {
		setProperty(KEY_TYPE, type != null ? type.toString() : null);
	}

}
