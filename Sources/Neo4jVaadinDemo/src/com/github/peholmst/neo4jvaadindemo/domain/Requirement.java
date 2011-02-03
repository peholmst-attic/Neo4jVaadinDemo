package com.github.peholmst.neo4jvaadindemo.domain;

public interface Requirement extends AggregateRoot, Nameable, Describable {

	enum Type {
		FUNCTIONAL, NONFUNCTIONAL
	}

	Type getType();

	void setType(Type type);
}
