package com.github.peholmst.neo4jvaadindemo.article3.domain;

import com.github.peholmst.neo4jvaadindemo.article3.domain.util.Describable;
import com.vaadin.data.Item;

public interface RequirementAssociation extends Describable, Item {

    enum Type {
        DUPLICATES, EXTENDS, CONFLICTS_WITH, RELATES_TO
    }
    
    static final String PROP_SOURCE = "source";
    
    Requirement getSource();
    
    static final String PROP_DESTINATION = "destination";
    
    Requirement getDestination();
    
    static final String PROP_TYPE = "type";
    
    Type getType();
    
    void setType(Type type);
}
