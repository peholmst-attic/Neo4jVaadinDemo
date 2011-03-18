package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import org.neo4j.graphdb.GraphDatabaseService;

public interface GraphDatabaseServiceProvider extends java.io.Serializable {

    GraphDatabaseService getGraphDb();
    
}
