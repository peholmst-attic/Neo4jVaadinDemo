package com.github.peholmst.neo4jvaadindemo.domain;


public interface AggregateRoot {

	long getId();
	
	void commitChanges();

	void discardChanges();
	
	public boolean hasUncommittedChanges();
	
	public boolean isInvalid();
	
	void delete();
	
}
