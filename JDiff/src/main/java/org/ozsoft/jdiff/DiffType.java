package org.ozsoft.jdiff;

/**
 * Diff types of a single line of text.
 * 
 * @author Oscar Stigter
 */
public enum DiffType {
    
    /** This line is identical in both files. */
	IDENTICAL,
    
    /** This line was added. */
	ADDED,
    
    /** This line was modified. */
    MODIFIED,
    
    /** This line was deleted. */
    DELETED,

}
