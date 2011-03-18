package com.github.peholmst.neo4jvaadindemo.article3.domain;

import com.github.peholmst.neo4jvaadindemo.article3.domain.util.Describable;
import com.vaadin.data.Item;

public interface UseCaseAssociation extends Describable, Item {

    enum Type {
        EXTENDS, INCLUDES, DUPLICATES, RELATES_TO
    }
    
    static final String PROP_SOURCE = "source";
    
    UseCase getSource();
    
    static final String PROP_DESTINATION = "destination";
    
    UseCase getDestination();
    
    static final String PROP_TYPE = "type";
    
    Type getType();
    
    void setType(Type type);
    
}