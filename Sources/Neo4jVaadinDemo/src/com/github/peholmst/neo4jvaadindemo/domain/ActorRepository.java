package com.github.peholmst.neo4jvaadindemo.domain;

import java.util.Collection;
import java.util.Iterator;

public interface ActorRepository {

	Actor createActor();
	
	Collection<Actor> getActors();
	
	Iterator<Actor> getActorIterator();	
	
}
