package org.ozsoft.taskman.domain;

/**
 * Task status entity.
 * 
 * @author Oscar Stigter
 */
public enum Status {
    
    OPEN("Open"),
    
    IN_PROGRESS("In Progress"),
    
    COMPLETED("Completed"),
    
    ;
    
    private final String name;
    
    Status(String name) {
	this.name = name;
    }
    
    public String getName() {
	return name;
    }

}
