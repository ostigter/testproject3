package org.ozsoft.taskman.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * User entity.
 * 
 * @author Oscar Stigter
 */
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @Basic
    @Column(nullable = false, unique = true)
    private String username;
    
    @Basic
    @Column(nullable = false)
    private String password;
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Task> tasks;
    
    public long getId() {
	return id;
    }

    public void setId(long id) {
	this.id = id;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }
    
    public List<Task> getTasks() {
	return tasks;
    }
    
    public void setTasks(List<Task> tasks) {
	this.tasks = tasks;
    }
    
    public void addTask(Task task) {
	if (tasks == null) {
	    tasks = new ArrayList<Task>();
	}
	tasks.add(task);
    }
    
    @Override
    public int hashCode() {
	return username.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
	if (obj instanceof User) {
	    return ((User) obj).username.equals(username);
	} else {
	    return false;
	}
    }

    @Override
    public String toString() {
	return username;
    }
    
}
