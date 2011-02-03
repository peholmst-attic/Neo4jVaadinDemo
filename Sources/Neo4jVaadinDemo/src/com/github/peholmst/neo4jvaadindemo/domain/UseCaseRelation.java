package com.github.peholmst.neo4jvaadindemo.domain;

public interface UseCaseRelation extends Describable {

	enum Type {
		EXTENDS, INCLUDES, RELATES_TO, DUPLICATES
	}

	Type getType();

	void setType(Type type);

	UseCase getSource();

	void setSource(UseCase source);

	UseCase getDestination();

	void setDestination(UseCase destination);
}
