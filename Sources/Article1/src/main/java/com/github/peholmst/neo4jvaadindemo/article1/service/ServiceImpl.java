package com.github.peholmst.neo4jvaadindemo.article1.service;

import com.github.peholmst.neo4jvaadindemo.article1.Backend;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
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
    }

    public static Service getInstance() {
        return INSTANCE;
    }

    protected GraphDatabaseService getGraphDb() {
        return Backend.getInstance().getGraphDb();
    }

    @Override
    public void storeMessage(String message) {
        log.info("Storing message \"{}\"", message);
        Transaction tx = getGraphDb().beginTx();
        try {
            // Parse message
            Set<String> tags = Utils.parseTags(message);
            // Store message
            Node messageNode = getGraphDb().createNode();
            messageNode.setProperty("message", message);
            getMessagesNode().createRelationshipTo(messageNode,
                    MyRelationshipTypes.CONTAINS_MESSAGE);
            // Store nodes
            for (String tag : tags) {
                log.info("Found tag: {}", tag);
                Node tagNode = getNodeForTag(tag);
                messageNode.createRelationshipTo(tagNode,
                        MyRelationshipTypes.IS_TAGGED_BY);
            }
            // Commit transaction
            tx.success();
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
            log.info("Creating a new root node for storing messages");
            Node messagesNode = getGraphDb().createNode();
            getGraphDb().getReferenceNode().createRelationshipTo(messagesNode,
                    MyRelationshipTypes.MESSAGES);
            return messagesNode;
        }
    }

    private Node getTagsNode() {
        Relationship tagsNodeRelationship = getGraphDb().getReferenceNode().
                getSingleRelationship(MyRelationshipTypes.TAGS,
                Direction.OUTGOING);
        if (tagsNodeRelationship != null) {
            return tagsNodeRelationship.getEndNode();
        } else {
            log.info("Creating a new root node for storing tags");
            Node tagsNode = getGraphDb().createNode();
            getGraphDb().getReferenceNode().createRelationshipTo(tagsNode,
                    MyRelationshipTypes.TAGS);
            return tagsNode;
        }
    }

    private Index<Node> getTagIndex() {
        return getGraphDb().index().forNodes("tags");
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
    public Collection<String> getTaggedMessages(String tag) {
        if (tag == null || tag.isEmpty()) {
            log.info("Empty tag found, returning an empty list");
            return Collections.emptyList();
        }
        log.info("Fetching messages tagged with {}", tag);
        Node tagNode = getNodeForTag(tag); // Never null
        Traverser traverser = tagNode.traverse(Traverser.Order.BREADTH_FIRST,
                StopEvaluator.DEPTH_ONE,
                ReturnableEvaluator.ALL_BUT_START_NODE,
                MyRelationshipTypes.IS_TAGGED_BY,
                Direction.INCOMING);
        LinkedList<String> list = new LinkedList<String>();
        for (Node node : traverser) {
            list.add((String) node.getProperty("message"));
        }
        return list;
    }

    @Override
    public Collection<String> getTagsStartingWith(String tagprefix) {
        if (tagprefix == null || tagprefix.isEmpty()) {
            log.info("Empty tag prefix found, returning an empty list");
            return Collections.emptyList();
        }
        log.info("Fetching tags that begin with {}", tagprefix);
        final IndexHits<Node> hits = getTagIndex().query("tag",
                tagprefix + "*");
        LinkedList<String> list = new LinkedList<String>();
        for (Node node : hits) {
            list.add((String) node.getProperty("tag"));
        }
        return list;
    }
}
