package com.github.peholmst.neo4jvaadindemo.article3.domain;

import com.vaadin.data.Item;

public interface UseCaseActor extends Item {

    enum Type {
        PRIMARY, SECONDARY
    }
    
    static final String PROP_ACTOR = "actor";
    
    Actor getActor();
    
    static final String PROP_USE_CASE = "useCase";
    
    UseCase getUseCase();
    
    static final String PROP_TYPE = "type";
    
    Type getType();
}
