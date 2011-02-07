package com.github.peholmst.neo4jvaadindemo.domain.impl;

import org.neo4j.graphdb.GraphDatabaseService;

public interface GraphDatabaseServiceProvider extends java.io.Serializable {

	GraphDatabaseService getGraphDatabaseService();

}
