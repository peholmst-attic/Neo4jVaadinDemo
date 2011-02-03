package com.github.peholmst.neo4jvaadindemo.domain;

public interface UseCaseScenario extends Nameable {

	enum Type {
		MAIN_SUCCESS_SCENARIO, EXTENSION
	}

	Type getType();

	void setType(Type type);
	
	String getSteps();

	void setSteps(String steps);
	
	UseCase getUseCase();	
}
