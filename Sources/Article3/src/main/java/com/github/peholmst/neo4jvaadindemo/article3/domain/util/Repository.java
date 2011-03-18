package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

public interface Repository<T extends AggregateRoot> {
    
    T create();
    
    CollectionFacade<T> getAll();
    
}
