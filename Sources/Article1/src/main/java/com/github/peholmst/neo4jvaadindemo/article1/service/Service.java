package com.github.peholmst.neo4jvaadindemo.article1.service;

import java.util.Collection;

/**
 * Service interface for this demo application.
 *
 * @author Petter Holmström
 */
public interface Service {

    /**
     * Stores the specified message. The message may contain hash tags (e.g. "#foo #bar").
     * @param message the message to store.
     */
    void storeMessage(String message);

    /**
     * Gets all the messages that have the specified tag.
     * @param tag the tag.
     * @return a collection of messages (never <code>null</code> but may be empty).
     */
    Collection<String> getTaggedMessages(String tag);

    /**
     * Gets all the tags that begin with the specified prefix.
     * @param tagprefix the prefix of the tag.
     * @return a collection of tags (never <code>null</code> but may be empty).
     */
    Collection<String> getTagsStartingWith(String tagprefix);
}
