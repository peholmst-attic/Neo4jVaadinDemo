package com.github.peholmst.neo4jvaadindemo.domain;


public interface AggregateRoot {

	long getIdentifier();
	
	void commitChanges();

	void discardChanges();
	
	public boolean hasUncommittedChanges();
	
	public boolean isInvalid();
	
	void delete();
	
}
