package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;

import com.github.peholmst.neo4jvaadindemo.domain.Requirement;
import com.github.peholmst.neo4jvaadindemo.domain.Scope;
import com.github.peholmst.neo4jvaadindemo.domain.Stakeholder;
import com.github.peholmst.neo4jvaadindemo.domain.UseCase;
import com.github.peholmst.neo4jvaadindemo.domain.UseCaseRelation;
import com.github.peholmst.neo4jvaadindemo.domain.UseCaseRequirementRelation;
import com.github.peholmst.neo4jvaadindemo.domain.UseCaseScenario;
import com.github.peholmst.neo4jvaadindemo.domain.UseCaseStakeholderInterest;

public class UseCaseImpl extends BaseAggregateRoot implements UseCase {

	private static final long serialVersionUID = 5090480461321510049L;

	protected static final String KEY_NAME = "name";
	protected static final String KEY_PRECONDITIONS = "preconditions";
	protected static final String KEY_SUCCESS_GUARANTEE = "successGuarantee";

	public UseCaseImpl(Node wrappedNode, long id,
			GraphDatabaseServiceProvider serviceProvider) {
		super(wrappedNode, id, serviceProvider);
	}

	public UseCaseImpl(Node wrappedNode,
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
	public Scope getScope() {
		final Relationship scopeRel = getNode().getSingleRelationship(
				RelationshipTypes.USECASE_TO_SCOPE, Direction.OUTGOING);
		if (scopeRel == null) {
			return null;
		} else {
			return new ScopeImpl(scopeRel.getStartNode(), getServiceProvider());
		}
	}

	@Override
	public void setScope(Scope scope) {
		final Relationship scopeRel = getNode().getSingleRelationship(
				RelationshipTypes.USECASE_TO_SCOPE, Direction.OUTGOING);
		if (scopeRel != null) {
			scopeRel.delete();
		}
		if (scope != null) {
			getNode().createRelationshipTo(((ScopeImpl) scope).getNode(),
					RelationshipTypes.USECASE_TO_SCOPE);
		}
	}

	@Override
	public String getPreconditions() {
		return (String) getProperty(KEY_PRECONDITIONS);
	}

	@Override
	public void setPreconditions(String preconditions) {
		setProperty(KEY_PRECONDITIONS, preconditions);
	}

	@Override
	public String getSuccessGuarantee() {
		return (String) getProperty(KEY_SUCCESS_GUARANTEE);
	}

	@Override
	public void setSuccessGuarantee(String successGuarantee) {
		setProperty(KEY_SUCCESS_GUARANTEE, successGuarantee);
	}

	protected Collection<Node> getScenarioNodes() {
		Traverser traverser = getNode().traverse(Traverser.Order.BREADTH_FIRST,
				StopEvaluator.DEPTH_ONE,
				ReturnableEvaluator.ALL_BUT_START_NODE,
				RelationshipTypes.USECASE_TO_SCENARIO, Direction.OUTGOING);
		return traverser.getAllNodes();
	}

	@Override
	public Collection<UseCaseScenario> getScenarios() {
		Collection<UseCaseScenario> scenarioCollection = new LinkedList<UseCaseScenario>();
		Collection<Node> scenarioNodes = getScenarioNodes();
		for (Node node : scenarioNodes) {
			scenarioCollection.add(new UseCaseScenarioImpl(node, this, getServiceProvider()));
		}
		return scenarioCollection;
	}

	@Override
	public UseCaseScenario addScenario(String name, UseCaseScenario.Type type,
			String steps) {
		// TODO Check that there is only one Main Success Scenario
		Node scenarioNode = getNode().getGraphDatabase().createNode();
		UseCaseScenarioImpl scenario = new UseCaseScenarioImpl(scenarioNode,
				this, getServiceProvider());
		getNode().createRelationshipTo(scenarioNode,
				RelationshipTypes.USECASE_TO_SCENARIO);
		return scenario;
	}

	@Override
	public void removeScenario(UseCaseScenario scenario) {
		// TODO Implement me!
		throw new UnsupportedOperationException("Not implemented yet!");
	}

	@Override
	public Collection<UseCaseRelation> getRelatedUseCases() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<UseCaseRelation> getRelatedUseCasesIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UseCaseRelation addRelatedUseCase(String description,
			UseCaseRelation.Type type, UseCase useCase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRelatedUseCase(UseCaseRelation useCase) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<UseCaseRequirementRelation> getRelatedRequirements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<UseCaseRequirementRelation> getRelatedRequirementsIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UseCaseRequirementRelation addRelatedRequirement(String description,
			UseCaseRequirementRelation.Type type, Requirement requirement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeRelatedRequirement(UseCaseRequirementRelation requirement) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<UseCaseStakeholderInterest> getStakeholdersAndInterests() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<UseCaseStakeholderInterest> getStakeholdersAndInterestsIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UseCaseStakeholderInterest addStakeholder(String interest,
			Stakeholder stakeholder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeStakeholder(UseCaseStakeholderInterest stakeholder) {
		// TODO Auto-generated method stub

	}

}
