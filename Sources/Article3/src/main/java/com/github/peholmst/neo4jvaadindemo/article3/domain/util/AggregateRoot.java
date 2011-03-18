package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

public interface AggregateRoot extends Entity {

    void commitChanges() throws ObjectInvalidException, OptimisticLockingException;

    void discardChanges() throws ObjectInvalidException, OptimisticLockingException;

    boolean hasUncommittedChanges();

    public boolean isInvalid();

    void delete() throws ObjectInvalidException, OptimisticLockingException;
}
