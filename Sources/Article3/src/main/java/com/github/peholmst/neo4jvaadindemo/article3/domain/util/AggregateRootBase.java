package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;

public abstract class AggregateRootBase extends EntityBase implements AggregateRoot {

    private long optimisticLockingVersion = -1;
    protected static final String KEY_OPTIMISTIC_LOCKING_VERSION = "optimisticLockingVersion";
    private static final Logger logger = Logger.getLogger(AggregateRootBase.class.getName());

    public AggregateRootBase(Node wrappedNode, GraphDatabaseServiceProvider graphDbProvider) {
        super(wrappedNode, graphDbProvider);
        if (!getNode().hasProperty(KEY_OPTIMISTIC_LOCKING_VERSION)) {
            getNode().setProperty(KEY_OPTIMISTIC_LOCKING_VERSION, 1L);
        }
        optimisticLockingVersion = (Long) getNode().getProperty(
                KEY_OPTIMISTIC_LOCKING_VERSION);
    }

    @Override
    protected void beforeCommitChanges() {
        checkOptimisticLocking();
    }

    @Override
    protected void afterCommitChanges() {
        getNode().setProperty(KEY_OPTIMISTIC_LOCKING_VERSION,
                ++optimisticLockingVersion);
    }

    @Override
    protected void beforeDelete() {
        checkOptimisticLocking();
    }

    private void checkOptimisticLocking() {
        try {
            if ((Long) getNode().getProperty(KEY_OPTIMISTIC_LOCKING_VERSION) != optimisticLockingVersion) {
                throw new OptimisticLockingException();
            }
        } catch (NotFoundException e) {
            logger.log(Level.FINE, "Error checking optimistic locking", e);
            invalidate();
            throw new ObjectInvalidException();
        }
    }

    @Override
    protected void afterDiscardChanges() {
        try {
            optimisticLockingVersion = (Long) getNode().getProperty(
                    KEY_OPTIMISTIC_LOCKING_VERSION);
        } catch (NotFoundException e) {
            logger.log(Level.FINE, "Error discarding changes", e);
            invalidate();
            throw new ObjectInvalidException();
        }
    }
}
