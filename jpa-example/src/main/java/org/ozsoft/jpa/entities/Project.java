package org.ozsoft.jpa.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * A project. <br />
 * <br />
 * 
 * A project belongs to a single user.
 * 
 * @author Oscar Stigter
 */
@Entity(name = "PROJ")
public class Project implements Serializable, Comparable<Project> {

    private static final long serialVersionUID = 3485262612752551153L;
    
    /** ID. */
    @Id
    @GeneratedValue
    @Column(name = "PROJ_ID")
    private long id;
    
    /** Name. */
    @Basic
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
    
    /** Owning user. */
    @ManyToOne()
    private User user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int compareTo(Project project) {
        return name.compareTo(project.getName());
    }
    
}
