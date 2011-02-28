package com.github.peholmst.neo4jvaadindemo.article2.service;

import com.github.peholmst.neo4jvaadindemo.article2.dto.MessageDTO;
import com.github.peholmst.neo4jvaadindemo.article2.dto.TagDTO;
import java.util.Collection;

/**
 * Service interface for this demo application.
 *
 * @author Petter Holmström
 */
public interface Service {

    /**
     * Stores the specified message.
     * @param message the message to store.
     * @return the stored message.
     */
    MessageDTO storeMessage(MessageDTO message);

    /**
     * Gets all the messages that have the specified tag.
     * @param tag the tag.
     * @return a collection of messages (never <code>null</code> but may be empty).
     */
    Collection<MessageDTO> getTaggedMessages(TagDTO tag);

    /**
     * Gets all the tags that begin with the specified prefix.
     * @param tagprefix the prefix of the tag.
     * @return a collection of tags (never <code>null</code> but may be empty).
     */
    Collection<TagDTO> getTagsStartingWith(String tagprefix);
}
