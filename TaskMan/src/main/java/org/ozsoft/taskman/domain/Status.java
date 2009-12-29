package org.ozsoft.taskman.domain;

/**
 * Task status entity.
 * 
 * @author Oscar Stigter
 */
public enum Status {
    
    /** Open. */
    OPEN("Open"),
    
    /** In progress. */
    IN_PROGRESS("In Progress"),
    
    /** Completed. */
    COMPLETED("Completed"),
    
    ;
    
    /** Name. */
    private final String name;
    
    /**
     * Constructor.
     * 
     * @param name
     *            The name.
     */
    Status(String name) {
	this.name = name;
    }
    
    /**
     * Returns the name.
     * 
     * @return The name.
     */
    public String getName() {
	return name;
    }
    
}
