package com.github.peholmst.neo4jvaadindemo.article2.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author Petter Holmström
 */
public class MessageDTO implements java.io.Serializable {

    private Long id;
    
    private Date timeOfCreation;
    
    private String content;

    private Set<TagDTO> tags = new HashSet<TagDTO>();
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        tags.clear();
        if (content != null) {
            for (String tag : parseTags(content)) {
                tags.add(new TagDTO(tag));
            }
        }
    }

    private static Set<String> parseTags(String message) {
        HashSet<String> tags = new HashSet<String>();
        StringTokenizer st = new StringTokenizer(message, " ");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.startsWith("#")) {
                tags.add(token.substring(1));
            }
        }
        return tags;
    }    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(Date timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MessageDTO other = (MessageDTO) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }    
    
}
