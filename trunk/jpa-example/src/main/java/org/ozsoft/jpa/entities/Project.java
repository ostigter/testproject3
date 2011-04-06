package org.ozsoft.jpa.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Project implements Serializable, Comparable<Project> {

    private static final long serialVersionUID = 3485262612752551153L;
    
    /** ID. */
    @Id
    @GeneratedValue
    @Column(name = "PROJECT_ID")
    private long id;
    
    /** Name. */
    @Basic
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
    
    /** Owning user. */
    @ManyToOne()
    @JoinColumn(name = "USER_ID")
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
