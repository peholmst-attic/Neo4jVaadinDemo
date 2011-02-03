package com.github.peholmst.neo4jvaadindemo.domain.impl;

import org.neo4j.graphdb.Node;

import com.github.peholmst.neo4jvaadindemo.domain.UseCase;
import com.github.peholmst.neo4jvaadindemo.domain.UseCaseScenario;

public class UseCaseScenarioImpl extends BaseNodeWrapper implements UseCaseScenario {

	private static final long serialVersionUID = 349298114085486236L;
	protected static final String KEY_NAME = "name";
	protected static final String KEY_TYPE = "type";
	protected static final String KEY_STEPS = "steps";
	
	private final UseCase owner;
	
	public UseCaseScenarioImpl(Node wrappedNode, UseCase owner, GraphDatabaseServiceProvider serviceProvider) {
		super(wrappedNode, serviceProvider);
		this.owner = owner;
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

	@Override
	public String getSteps() {
		return (String) getProperty(KEY_STEPS);
	}

	@Override
	public void setSteps(String steps) {
		setProperty(KEY_STEPS, steps);
	}

	@Override
	public UseCase getUseCase() {
		return owner;
	}

}
