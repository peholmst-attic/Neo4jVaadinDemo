package com.github.peholmst.neo4jvaadindemo.domain.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;

import com.github.peholmst.neo4jvaadindemo.domain.ObjectInvalidException;
import com.github.peholmst.neo4jvaadindemo.domain.OptimisticTransactionLockingException;
import com.github.peholmst.neo4jvaadindemo.domain.impl.GraphDatabaseServiceProvider.TransactionJob;

public abstract class BaseNodeWrapper implements java.io.Serializable {

	// TODO Change logging levels to FINE or finer

	private static final long serialVersionUID = 77458242567898449L;

	private transient Node wrappedNode;

	private long wrappedNodeId;

	private final GraphDatabaseServiceProvider serviceProvider;

	private final Map<String, Object> propertyValueCache = new HashMap<String, Object>();

	private transient Logger logger = Logger.getLogger(getClass().getName());

	private long optimisticLockingVersion = -1;

	protected static final String KEY_OPTIMISTIC_LOCKING_VERSION = "optimisticLockingVersion";

	public BaseNodeWrapper(Node wrappedNode,
			GraphDatabaseServiceProvider serviceProvider) {
		if (wrappedNode == null) {
			throw new IllegalArgumentException("null wrappedNode");
		}
		if (serviceProvider == null) {
			throw new IllegalArgumentException("null serviceProvider");
		}
		wrappedNodeId = wrappedNode.getId();
		this.wrappedNode = wrappedNode;
		this.serviceProvider = serviceProvider;
		if (!getNode().hasProperty(KEY_OPTIMISTIC_LOCKING_VERSION)) {
			getNode().setProperty(KEY_OPTIMISTIC_LOCKING_VERSION, 1L);
		}
		optimisticLockingVersion = (Long) getNode().getProperty(
				KEY_OPTIMISTIC_LOCKING_VERSION);
	}

	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		if (wrappedNodeId != -1) {
			getServiceProvider().runInsideTransaction(new TransactionJob() {

				@Override
				public Object run() throws RuntimeException {
					wrappedNode = serviceProvider.getGraphDatabaseService()
							.getNodeById(wrappedNodeId);
					return null;
				}
			}, true);
		}
		this.logger = Logger.getLogger(getClass().getName());
	}

	protected final GraphDatabaseServiceProvider getServiceProvider() {
		return serviceProvider;
	}

	protected Node getNode() {
		return wrappedNode;
	}

	/*
	 * @Override public boolean equals(Object obj) { if (obj == null) { return
	 * false; } if (obj.getClass() != getClass()) { return false; } if (obj ==
	 * this) { return true; } if (isInvalid()) { return false; } final
	 * BaseNodeWrapper other = (BaseNodeWrapper) obj; if
	 * (!propertyValueCache.equals(other.propertyValueCache)) { return false; }
	 * return (Boolean) getServiceProvider().runInsideTransaction( new
	 * TransactionJob() {
	 * 
	 * @Override public Object run() throws RuntimeException { return
	 * getNode().equals(other.getNode()); }
	 * 
	 * }, true); }
	 * 
	 * @Override public int hashCode() { if (isInvalid()) { return 0; } return
	 * (Integer) getServiceProvider().runInsideTransaction( new TransactionJob()
	 * {
	 * 
	 * @Override public Object run() throws RuntimeException { return
	 * getNode().hashCode() + propertyValueCache.hashCode(); }
	 * 
	 * }, true); }
	 */

	public static <E, T extends E> Collection<E> wrapNodeCollection(
			Collection<Node> nodeCollection, Class<E> interfaceClass,
			Class<T> baseNodeWrapperClass,
			GraphDatabaseServiceProvider serviceProvider) {
		Collection<E> wrappedCollection = new LinkedList<E>();
		for (Node node : nodeCollection) {
			try {
				wrappedCollection.add(baseNodeWrapperClass.getConstructor(
						Node.class, GraphDatabaseServiceProvider.class)
						.newInstance(node, serviceProvider));
			} catch (Exception e) {
				throw new IllegalArgumentException(
						"Illegal baseNodeWrapperClass");
			}
		}
		return wrappedCollection;
	}

	protected Object getProperty(final String propertyName) {
		if (isInvalid()) {
			logger.log(Level.WARNING,
					"Attempting to get property {0} on invalid object {1}",
					new Object[] { propertyName, this });
			return null;
		}
		if (propertyValueCache.containsKey(propertyName)) {
			return propertyValueCache.get(propertyName);
		}
		try {
			return getServiceProvider().runInsideTransaction(
					new TransactionJob() {

						@Override
						public Object run() throws RuntimeException {
							if (getNode().hasProperty(propertyName)) {
								return getNode().getProperty(propertyName);
							} else {
								return null;
							}
						}

					}, true);
		} catch (NotFoundException e) {
			invalidate();
			throw new ObjectInvalidException("Object is invalid");
		}
	}

	protected void setProperty(String propertyName, Object value) {
		if (isInvalid()) {
			logger.log(Level.WARNING,
					"Attempting to set property {0} on invalid object {1}",
					new Object[] { propertyName, this });
			return;
		}
		propertyValueCache.put(propertyName, value);
	}

	public void commitChanges() {
		if (isInvalid()) {
			throw new ObjectInvalidException(
					"Cannot commit changes to an invalid object");
		}
		logger.log(Level.INFO, "Commiting changes to object {0}", this);
		getServiceProvider().runInsideTransaction(new TransactionJob() {

			@Override
			public Object run() {
				doCommit();
				return null;
			}

		}, false);
	}

	protected void doCommit() {
		try {
			if ((Long) getNode().getProperty(KEY_OPTIMISTIC_LOCKING_VERSION) != optimisticLockingVersion) {
				throw new OptimisticTransactionLockingException();
			}
			for (Map.Entry<String, Object> entry : propertyValueCache
					.entrySet()) {
				if (entry.getValue() == null) {
					getNode().removeProperty(entry.getKey());
				} else {
					getNode().setProperty(entry.getKey(), entry.getValue());
				}
			}
			getNode().setProperty(KEY_OPTIMISTIC_LOCKING_VERSION,
					++optimisticLockingVersion);
		} catch (NotFoundException e) {
			invalidate();
			throw new ObjectInvalidException("Object is invalid");
		}
		propertyValueCache.clear();
	}

	public void delete() {
		if (isInvalid()) {
			throw new ObjectInvalidException("Cannot delete an invalid object");
		}
		getServiceProvider().runInsideTransaction(new TransactionJob() {

			@Override
			public Object run() throws RuntimeException {
				doDelete();
				return null;
			}
		}, false);
		invalidate();
	}

	protected void doDelete() {
		getNode().delete();
	}

	protected void invalidate() {
		logger.log(Level.INFO, "Invalidating object {0}", this);
		wrappedNode = null;
		wrappedNodeId = -1;
		optimisticLockingVersion = -1;
		propertyValueCache.clear();
	}

	public void discardChanges() {
		if (isInvalid()) {
			throw new ObjectInvalidException(
					"Cannot discard changes to an invalid object");
		}
		logger.log(Level.INFO, "Discarding changes to object {0}", this);
		propertyValueCache.clear();
		getServiceProvider().runInsideTransaction(new TransactionJob() {

			@Override
			public Object run() throws RuntimeException {
				optimisticLockingVersion = (Long) getNode().getProperty(
						KEY_OPTIMISTIC_LOCKING_VERSION);
				return null;
			}
		}, true);
	}

	public boolean hasUncommittedChanges() {
		return !isInvalid() && !propertyValueCache.isEmpty();
	}

	public boolean isInvalid() {
		return wrappedNode == null;
	}
}
