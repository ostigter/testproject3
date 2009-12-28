package org.ozsoft.taskman.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Task entity.
 * 
 * @author Oscar Stigter
 */
@Entity
public class Task {
    
    @Id
    private long id;
    
    private String summary;
    
    private Status status;
    
    public long getId() {
	return id;
    }
    
    public void setId(long id) {
	this.id = id;
    }
    
    public String getSummary() {
	return summary;
    }
    
    public void setSummary(String summary) {
	this.summary = summary;
    }

    public Status getStatus() {
	return status;
    }
    
    public void setStatus(Status status) {
	this.status = status;
    }

}
