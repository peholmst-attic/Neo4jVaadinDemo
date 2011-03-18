package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import com.github.peholmst.neo4jvaadindemo.article3.util.ItemPropertyUtil;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Transaction;

public abstract class EntityBase implements Entity, Item {

    private transient Node wrappedNode;
    private final UUID identifier;
    private long wrappedNodeId;
    private final GraphDatabaseServiceProvider graphDbProvider;
    private final Map<String, Object> propertyValueCache = new HashMap<String, Object>();
    private static final Logger logger = Logger.getLogger(EntityBase.class.getName());
    
    private Collection<String> itemPropertyIds;
    private Map<String, Property> itemProperties;

    protected EntityBase(Node wrappedNode, GraphDatabaseServiceProvider graphDbProvider) {
        if (wrappedNode == null) {
            throw new IllegalArgumentException("null wrappedNode");
        }
        if (graphDbProvider == null) {
            throw new IllegalArgumentException("null graphDbProvider");
        }
        wrappedNodeId = wrappedNode.getId();
        this.wrappedNode = wrappedNode;
        this.graphDbProvider = graphDbProvider;
        if (!getNode().hasProperty(PROP_IDENTIFIER)) {
            getNode().setProperty(PROP_IDENTIFIER, UUID.randomUUID().toString());
        }
        this.identifier = UUID.fromString((String) getNode().getProperty(PROP_IDENTIFIER));
        initItemProperties();
    }
    
    private void initItemProperties() {
        this.itemPropertyIds = Collections.unmodifiableCollection(ItemPropertyUtil.getItemPropertyIdsFromClass(getClass(), true));
        this.itemProperties = new HashMap<String, Property>();
        for (String itemPropertyId : itemPropertyIds) {
            itemProperties.put(itemPropertyId, ItemPropertyUtil.createProperty(itemPropertyId, this));
        }        
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (wrappedNodeId != -1) {
            try {
                wrappedNode = getGraphDb().getNodeById(wrappedNodeId);
                if (!getNode().hasProperty(PROP_IDENTIFIER)
                        || !identifier.toString().equals(getNode().getProperty(PROP_IDENTIFIER))) {
                    invalidate();
                }
            } catch (NotFoundException e) {
                invalidate();
            }
        }
    }

    protected final void invalidate() {
        logger.log(Level.WARNING, "Invalidating entity {0}", this);
        wrappedNode = null;
        wrappedNodeId = -1;
        propertyValueCache.clear();
    }

    protected final Node getNode() {
        return wrappedNode;
    }

    protected final GraphDatabaseService getGraphDb() {
        return graphDbProvider.getGraphDb();
    }

    @Override
    public final UUID getIdentifier() {
        return identifier;
    }

    @Override
    public final boolean isInvalid() {
        return wrappedNode == null;
    }

    protected final void setProperty(String propertyName, Object value) {
        if (isInvalid()) {
            logger.log(Level.WARNING, "Attempted to set property {0} of invalid object {1}",
                    new Object[]{propertyName, this});
            throw new ObjectInvalidException();
        }
        logger.log(Level.FINE, "Attempting to set property {0} of object {1}", new Object[]{propertyName, this});
        propertyValueCache.put(propertyName, value);
    }

    protected final Object getProperty(String propertyName) {
        if (isInvalid()) {
            logger.log(Level.WARNING, "Attempted to get property {0} of invalid object {1}", new Object[]{propertyName, this});
            throw new ObjectInvalidException();
        }
        logger.log(Level.FINE, "Attempting to get property {0} of object {1}", new Object[]{propertyName, this});
        if (propertyValueCache.containsKey(propertyName)) {
            return propertyValueCache.get(propertyName);
        }
        try {
            if (getNode().hasProperty(propertyName)) {
                return getNode().getProperty(propertyName);
            } else {
                return null;
            }
        } catch (NotFoundException e) {
            logger.log(Level.FINE, "Error accessing node property", e);
            invalidate();
            throw new ObjectInvalidException();
        }
    }

    public final boolean hasUncommittedChanges() {
        return !isInvalid() && !propertyValueCache.isEmpty();
    }

    public final void discardChanges() {
        if (isInvalid()) {
            logger.log(Level.WARNING, "Attempted to discard changes to an invalid object {0}", this);
            throw new ObjectInvalidException();
        }
        logger.log(Level.INFO, "Discarding changes to object {0}", this);
        beforeDiscardChanges();
        propertyValueCache.clear();
        afterDiscardChanges();
    }

    protected void beforeDiscardChanges() {
    }

    protected void afterDiscardChanges() {
    }

    protected void beforeCommitChanges() {
    }

    protected void afterCommitChanges() {
    }

    protected void beforeDelete() {
    }

    protected void afterDelete() {
    }

    public final void delete() {
        if (isInvalid()) {
            logger.log(Level.WARNING, "Attempted to delete an invalid object {0}", this);
            throw new ObjectInvalidException();
        }
        logger.log(Level.INFO, "Deleting object {0}", this);
        Transaction tx = getGraphDb().beginTx();
        try {
            beforeDelete();
            getNode().delete();
            afterDelete();
            tx.success();
        } finally {
            tx.finish();
        }
    }

    private void flushCacheToNode() {
        for (Map.Entry<String, Object> entry : propertyValueCache.entrySet()) {
            if (entry.getValue() == null) {
                getNode().removeProperty(entry.getKey());
            } else {
                getNode().setProperty(entry.getKey(), entry.getValue());
            }
        }
        propertyValueCache.clear();
    }

    public final void commitChanges() {
        if (isInvalid()) {
            logger.log(Level.WARNING, "Attempted to commit changes to an invalid object {0}", this);
            throw new ObjectInvalidException();
        }
        logger.log(Level.INFO, "Commiting changes to object {0}", this);
        Transaction tx = getGraphDb().beginTx();
        try {
            beforeCommitChanges();
            flushCacheToNode();
            afterCommitChanges();
            tx.success();
        } finally {
            tx.finish();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        if (isInvalid()) {
            return ((EntityBase) obj).isInvalid();
        }
        return getNode().getId() == ((EntityBase) obj).getNode().getId();
    }

    @Override
    public int hashCode() {
        if (isInvalid()) {
            return 0;
        } else {
            return getNode().hashCode();
        }
    }

    @Override
    public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Item properties cannot be added");
    }

    @Override
    public Property getItemProperty(Object id) {
        if (!getItemPropertyIds().contains(id)) {
            return null;
        }
        return itemProperties.get((String) id);
    }

    @Override
    public Collection<?> getItemPropertyIds() {
        return itemPropertyIds;
    }

    @Override
    public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Item properties cannot be removed");
    }
}
