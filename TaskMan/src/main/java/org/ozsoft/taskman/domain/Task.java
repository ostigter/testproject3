package org.ozsoft.taskman.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Task entity.
 * 
 * @author Oscar Stigter
 */
@Entity
public class Task {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;
    
    @Basic
    @Column(nullable = false)
    private String summary;

    // ### Does not work with JPA 1.0 ###
//    @ManyToOne(fetch = FetchType.LAZY)
//    private User user;
    
    @Basic
    @Column(nullable = false)
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

//    public User getUser() {
//	return user;
//    }
//    
//    public void setUser(User user) {
//	this.user = user;
//    }
    
    public Status getStatus() {
	return status;
    }
    
    public void setStatus(Status status) {
	this.status = status;
    }

}
