package com.github.peholmst.neo4jvaadindemo.article3.domain;

import com.github.peholmst.neo4jvaadindemo.article3.domain.util.Describable;
import com.github.peholmst.neo4jvaadindemo.article3.util.ItemProperty;
import com.github.peholmst.neo4jvaadindemo.article3.util.NestedItemProperty;
import com.vaadin.data.Item;

public interface RequirementAssociation extends Describable, Item {

    enum Type {

        DUPLICATES, EXTENDS, CONFLICTS_WITH, RELATES_TO
    }
    static final String PROP_SOURCE = "source";
    static final String PROP_DESTINATION = "destination";
    static final String PROP_TYPE = "type";

    @ItemProperty(nestedProperties =
    @NestedItemProperty(propertyName = Requirement.PROP_NAME))
    Requirement getSource();

    @ItemProperty(nestedProperties =
    @NestedItemProperty(propertyName = Requirement.PROP_NAME))
    Requirement getDestination();

    @ItemProperty
    Type getType();

    void setType(Type type);
}
