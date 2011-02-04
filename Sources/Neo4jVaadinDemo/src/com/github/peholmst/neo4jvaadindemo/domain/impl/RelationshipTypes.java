package com.github.peholmst.neo4jvaadindemo.domain.impl;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {

	SR_ACTORS, SR_ACTOR, SR_STAKEHOLDERS, SR_STAKEHOLDER, SR_REQUIREMENTS, SR_REQUIREMENT,
	SR_SCOPES, SR_SCOPE, SR_USECASES, SR_USECASE, USECASE_TO_SCOPE, USECASE_TO_SCENARIO,
	EXTENDS_ACTOR
}
