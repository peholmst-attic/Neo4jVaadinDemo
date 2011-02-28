package com.github.peholmst.neo4jvaadindemo.article2.service;

import com.github.peholmst.neo4jvaadindemo.article2.Backend;
import com.github.peholmst.neo4jvaadindemo.article2.dto.MessageDTO;
import com.github.peholmst.neo4jvaadindemo.article2.dto.TagDTO;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.kernel.impl.transaction.LockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton implementation of the {@link Service} interface.
 * 
 * @author Petter Holmström
 */
public class ServiceImpl implements Service {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static Service INSTANCE = new ServiceImpl();

    private ServiceImpl() {
        setupSystemNodes();
    }

    public static Service getInstance() {
        return INSTANCE;
    }

    protected GraphDatabaseService getGraphDb() {
        return Backend.getInstance().getGraphDb();
    }
    
    protected LockManager getLockManager() {
        return Backend.getInstance().getLockManager();
    }
    
    protected long getNextIdentifier() {
        log.info("Retrieving next identifier");
        Transaction tx = getGraphDb().beginTx();
        try {
            Node identifierNode = getIdentifierNode();
            getLockManager().getWriteLock(identifierNode);
            long id = 0L;
            if (identifierNode.hasProperty("id")) {
                id = (Long) identifierNode.getProperty("id");
            }
            id++;
            identifierNode.setProperty("id", id);
            tx.success();
            return id;
        } finally {
            tx.finish();
        }
    }
    
    private void setupSystemNodes() {
        // Setup some system nodes
        log.info("Setting up system nodes");
        Transaction tx = getGraphDb().beginTx();
        try {
            getLockManager().getWriteLock(getGraphDb().getReferenceNode());
            createNodeIfNotExists(MyRelationshipTypes.IDENTIFIER);
            createNodeIfNotExists(MyRelationshipTypes.MESSAGES);
            createNodeIfNotExists(MyRelationshipTypes.TAGS);            
            tx.success();
        } finally {
            tx.finish();
        }        
    }
    
    private void createNodeIfNotExists(RelationshipType relationshipType) {
        Relationship rel = getGraphDb().getReferenceNode().getSingleRelationship(relationshipType, Direction.OUTGOING);
        if (rel == null) {
            log.info("Creating system node {}", relationshipType);
            Node n = getGraphDb().createNode();
            getGraphDb().getReferenceNode().createRelationshipTo(n, relationshipType);
        }
    }

    private Node getIdentifierNode() {
        Relationship idRelationship = getGraphDb().getReferenceNode().getSingleRelationship(MyRelationshipTypes.IDENTIFIER, Direction.OUTGOING);
        if (idRelationship != null) {
            return idRelationship.getEndNode();
        } else {
            throw new IllegalStateException("Identifier node could not be found");
        }
    }
    
    @Override
    public MessageDTO storeMessage(MessageDTO message) {
        log.info("Storing message \"{}\"", message);
        // Update some values
        message.setId(getNextIdentifier());
        message.setTimeOfCreation(new Date());
 
        Transaction tx = getGraphDb().beginTx();
        try {            
            // Store message
            Node messageNode = getGraphDb().createNode();
            messageNode.setProperty("id", message.getId());
            messageNode.setProperty("message", message.getContent());
            messageNode.setProperty("timeOfCreation", message.getTimeOfCreation().getTime());
            getMessagesNode().createRelationshipTo(messageNode,
                    MyRelationshipTypes.CONTAINS_MESSAGE);
            
            // Store tags
            for (TagDTO tag : message.getTags()) {
                log.info("Stroing tag: {}", tag);
                Node tagNode = getNodeForTag(tag.getName());
                messageNode.createRelationshipTo(tagNode,
                        MyRelationshipTypes.IS_TAGGED_BY);
            }
            // Update index
            getMessageIndex().add(messageNode, "id", message.getId());
            
            // Commit transaction
            tx.success();
            // Return the DTO
            return message;
        } finally {
            tx.finish();
        }
    }

    private Node getMessagesNode() {
        Relationship messagesNodeRelationship = getGraphDb().getReferenceNode().
                getSingleRelationship(MyRelationshipTypes.MESSAGES,
                Direction.OUTGOING);
        if (messagesNodeRelationship != null) {
            return messagesNodeRelationship.getEndNode();
        } else {
            throw new IllegalStateException("Messages node could not be found");
        }
    }

    private Node getTagsNode() {
        Relationship tagsNodeRelationship = getGraphDb().getReferenceNode().
                getSingleRelationship(MyRelationshipTypes.TAGS,
                Direction.OUTGOING);
        if (tagsNodeRelationship != null) {
            return tagsNodeRelationship.getEndNode();
        } else {
            throw new IllegalStateException("Tags node could not be found");
        }
    }

    private Index<Node> getTagIndex() {
        return getGraphDb().index().forNodes("tags");
    }

    private Index<Node> getMessageIndex() {
        return getGraphDb().index().forNodes("messages");
    }
    
    private Node getNodeForTag(String tag) {
        IndexHits<Node> hits = getTagIndex().get("tag", tag);
        try {
            if (hits.hasNext()) {
                return hits.next();
            } else {
                log.info("Creating a new node for tag \"{}\"", tag);
                // We have to create a new node
                Node tagNode = getGraphDb().createNode();
                tagNode.setProperty("tag", tag);
                getTagsNode().createRelationshipTo(tagNode,
                        MyRelationshipTypes.CONTAINS_TAG);
                // Also remember to add it to the index
                getTagIndex().add(tagNode, "tag", tag);
                return tagNode;
            }
        } finally {
            hits.close();
        }
    }

    @Override
    public Collection<MessageDTO> getTaggedMessages(TagDTO tag) {
        if (tag == null || tag.getName().isEmpty()) {
            log.info("Empty tag found, returning an empty list");
            return Collections.emptyList();
        }
        log.info("Fetching messages tagged with {}", tag);
        Node tagNode = getNodeForTag(tag.getName()); // Never null
        Traverser traverser = tagNode.traverse(Traverser.Order.BREADTH_FIRST,
                StopEvaluator.DEPTH_ONE,
                ReturnableEvaluator.ALL_BUT_START_NODE,
                MyRelationshipTypes.IS_TAGGED_BY,
                Direction.INCOMING);
        LinkedList<MessageDTO> list = new LinkedList<MessageDTO>();
        for (Node node : traverser) {
            MessageDTO msg = new MessageDTO();
            msg.setId( (Long) node.getProperty("id"));
            msg.setTimeOfCreation(new Date( (Long) node.getProperty("timeOfCreation")));
            // This will automatically parse the message and create the necessary tag DTOs
            msg.setContent((String) node.getProperty("message"));
            list.add(msg);
        }
        return list;
    }

    @Override
    public Collection<TagDTO> getTagsStartingWith(String tagprefix) {
        if (tagprefix == null || tagprefix.isEmpty()) {
            log.info("Empty tag prefix found, returning an empty list");
            return Collections.emptyList();
        }
        log.info("Fetching tags that begin with {}", tagprefix);
        final IndexHits<Node> hits = getTagIndex().query("tag",
                tagprefix + "*");
        LinkedList<TagDTO> list = new LinkedList<TagDTO>();
        for (Node node : hits) {
            list.add(new TagDTO((String) node.getProperty("tag")));
        }
        return list;
    }

}
