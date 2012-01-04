package org.ozsoft.jboss.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Release implements Serializable {
    
    private static final long serialVersionUID = -2334607643073889386L;

    @Id
    @GeneratedValue
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
    
    @Basic
    private String name;
    
    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }
    
    /* package */ void setProject(Project project) {
    	this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        return name;
    }

}
