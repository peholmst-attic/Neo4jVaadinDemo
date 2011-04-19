package com.github.peholmst.neo4jvaadindemo.article3.domain.util;

import com.github.peholmst.neo4jvaadindemo.article3.util.ItemProperty;

/**
 * Interface to be implemented by any entity that has a <code>description</code> property.
 * @author Petter Holmström
 */
public interface Describable extends java.io.Serializable {

    String PROP_DESCRIPTION = "description";
    
    @ItemProperty
    String getDescription();
    
    void setDescription(String description);
}
