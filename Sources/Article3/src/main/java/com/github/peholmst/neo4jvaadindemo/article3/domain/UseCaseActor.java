package com.github.peholmst.neo4jvaadindemo.article3.domain;

import com.github.peholmst.neo4jvaadindemo.article3.util.ItemProperty;
import com.github.peholmst.neo4jvaadindemo.article3.util.NestedItemProperty;
import com.vaadin.data.Item;

public interface UseCaseActor extends Item {

    enum Type {

        PRIMARY, SECONDARY
    }
    static final String PROP_ACTOR = "actor";
    static final String PROP_USE_CASE = "useCase";
    static final String PROP_TYPE = "type";

    @ItemProperty(nestedProperties =
    @NestedItemProperty(propertyName = Actor.PROP_NAME))
    Actor getActor();

    @ItemProperty(nestedProperties =
    @NestedItemProperty(propertyName = UseCase.PROP_NAME))
    UseCase getUseCase();

    @ItemProperty
    Type getType();
}
