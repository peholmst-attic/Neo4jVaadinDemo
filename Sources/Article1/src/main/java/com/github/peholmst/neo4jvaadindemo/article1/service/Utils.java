package com.github.peholmst.neo4jvaadindemo.article1.service;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Utility class for various utility functions.
 * 
 * @author Petter Holmström
 */
public class Utils {

    private Utils() {
    }

    /**
     * Parses the specified string message and returns a set of hash tags (without the hashes) found in the message.
     * E.g. a message "hello #world #greeting" would return a set containing "world" and "greeting".
     * @param message the message to parse.
     * @return a set of hash tags.
     */
    public static Set<String> parseTags(String message) {
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
}
