package com.github.peholmst.neo4jvaadindemo.article3.domain;

import com.github.peholmst.neo4jvaadindemo.article3.domain.util.Describable;
import com.github.peholmst.neo4jvaadindemo.article3.domain.util.Entity;
import com.github.peholmst.neo4jvaadindemo.article3.util.ItemProperty;
import com.github.peholmst.neo4jvaadindemo.article3.domain.util.Nameable;
import com.github.peholmst.neo4jvaadindemo.article3.util.NestedItemProperty;
import com.vaadin.data.Item;

public interface Scenario extends Entity, Nameable, Describable, Item {

    enum Type {

        MAIN_SUCCESS_SCENARIO,
        EXTENSION
    }
    static final String PROP_USE_CASE = "useCase";
    static final String PROP_TYPE = "type";

    @ItemProperty(nestedProperties =
    @NestedItemProperty(propertyName = UseCase.PROP_NAME))
    UseCase getUseCase();

    @ItemProperty
    Type getType();
}
