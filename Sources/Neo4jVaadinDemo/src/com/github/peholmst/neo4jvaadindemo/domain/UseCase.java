package com.github.peholmst.neo4jvaadindemo.domain;

import java.util.Collection;
import java.util.Iterator;

public interface UseCase extends AggregateRoot, Nameable {

	Scope getScope();

	void setScope(Scope scope);

	String getPreconditions();

	void setPreconditions(String preconditions);

	String getSuccessGuarantee();

	void setSuccessGuarantee(String successGuarantee);

	Collection<UseCaseScenario> getScenarios();

	UseCaseScenario addScenario(String name, UseCaseScenario.Type type, String steps);

	void removeScenario(UseCaseScenario scenario);

	Collection<UseCaseRelation> getRelatedUseCases();

	Iterator<UseCaseRelation> getRelatedUseCasesIterator();

	UseCaseRelation addRelatedUseCase(String description,
			UseCaseRelation.Type type, UseCase useCase);

	void removeRelatedUseCase(UseCaseRelation useCase);

	Collection<UseCaseRequirementRelation> getRelatedRequirements();

	Iterator<UseCaseRequirementRelation> getRelatedRequirementsIterator();

	UseCaseRequirementRelation addRelatedRequirement(String description,
			UseCaseRequirementRelation.Type type, Requirement requirement);

	void removeRelatedRequirement(UseCaseRequirementRelation requirement);

	Collection<UseCaseStakeholderInterest> getStakeholdersAndInterests();

	Iterator<UseCaseStakeholderInterest> getStakeholdersAndInterestsIterator();

	UseCaseStakeholderInterest addStakeholder(String interest,
			Stakeholder stakeholder);

	void removeStakeholder(UseCaseStakeholderInterest stakeholder);

}
