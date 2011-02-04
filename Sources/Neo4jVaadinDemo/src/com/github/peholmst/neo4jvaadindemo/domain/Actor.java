package com.github.peholmst.neo4jvaadindemo.domain;

import java.util.Collection;

public interface Actor extends AggregateRoot, Nameable, Describable {
	
	void addSuperActor(Actor actor);
	
	void removeSuperActor(Actor actor);
	
	Collection<Actor> getSuperActors();
		
	Collection<Actor> getSubActors();
	
}
