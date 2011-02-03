package com.github.peholmst.neo4jvaadindemo.domain;

public class ObjectInvalidException extends IllegalStateException {

	private static final long serialVersionUID = 2620176598366404578L;

	public ObjectInvalidException() {
		super();
	}

	public ObjectInvalidException(String s) {
		super(s);
	}	

}
