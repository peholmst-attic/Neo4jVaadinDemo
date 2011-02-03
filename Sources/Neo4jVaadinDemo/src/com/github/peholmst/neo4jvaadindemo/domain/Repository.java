package com.github.peholmst.neo4jvaadindemo.domain;

import java.util.Collection;
import java.util.Iterator;

public interface Repository<T extends AggregateRoot> {

	T create();
	
	Collection<T> getAll();
	
	Iterator<T> getIterator();
	
}
