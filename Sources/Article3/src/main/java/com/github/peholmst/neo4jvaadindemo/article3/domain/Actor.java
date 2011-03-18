package com.github.peholmst.neo4jvaadindemo.article3.domain;

import com.github.peholmst.neo4jvaadindemo.article3.domain.util.AggregateRoot;
import com.github.peholmst.neo4jvaadindemo.article3.domain.util.CollectionFacade;
import com.github.peholmst.neo4jvaadindemo.article3.domain.util.Describable;
import com.github.peholmst.neo4jvaadindemo.article3.domain.util.Nameable;
import com.vaadin.data.Item;

public interface Actor extends AggregateRoot, Nameable, Describable, Item {

    void addSuperactor(Actor actor);
    
    void removeSuperactor(Actor actor);
    
    CollectionFacade<Actor> getSuperactors();
    
    CollectionFacade<Actor> getSubactors();
}
