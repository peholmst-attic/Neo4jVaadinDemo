package com.github.peholmst.neo4jvaadindemo.domain;

public interface UseCaseRequirementRelation extends Describable {

	enum Type {
		IMPLEMENTS, AFFECTED_BY, CONFLICTS_WITH, RELATES_TO, DUPLICATES
	}

	Requirement getRequirement();

	void setRequirement(Requirement requirement);

	UseCase getUseCase();

	void setUseCase(UseCase useCase);

	Type getType();

	void setType(Type type);
}
