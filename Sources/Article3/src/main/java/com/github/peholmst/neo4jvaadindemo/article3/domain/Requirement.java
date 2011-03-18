package com.github.peholmst.neo4jvaadindemo.article3.domain;

import com.github.peholmst.neo4jvaadindemo.article3.domain.util.AggregateRoot;
import com.github.peholmst.neo4jvaadindemo.article3.domain.util.CollectionFacade;
import com.github.peholmst.neo4jvaadindemo.article3.domain.util.Describable;
import com.github.peholmst.neo4jvaadindemo.article3.util.ItemProperty;
import com.github.peholmst.neo4jvaadindemo.article3.domain.util.Nameable;
import com.vaadin.data.Item;

public interface Requirement extends AggregateRoot, Nameable, Describable, Item {

    enum Type {

        FUNCTIONAL, NONFUNCTIONAL
    }
    static final String PROP_TYPE = "type";

    @ItemProperty
    Type getType();

    void setType(Type type);

    RequirementAssociation associateWithRequirement(Requirement destination);

    void removeAssociation(RequirementAssociation association);

    CollectionFacade<RequirementAssociation> getRequirementAssociations();

    CollectionFacade<UseCaseRequirementAssociation> getUseCaseAssociations();
}
