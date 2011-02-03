package com.github.peholmst.neo4jvaadindemo.domain;

public interface UseCaseStakeholderInterest {
	
	Stakeholder getStakeholder();

	void setStakeholder(Stakeholder stakeholder);

	String getInterest();

	void setInterest(String interest);

	UseCase getUseCase();

	void setUseCase(UseCase useCase);
}
