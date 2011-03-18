package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

public class OptimisticLockingException extends RuntimeException {

    public OptimisticLockingException(String string) {
        super(string);
    }

    public OptimisticLockingException() {
    }
    
}
