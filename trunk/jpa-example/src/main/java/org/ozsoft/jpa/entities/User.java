package org.ozsoft.jpa.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * User entity.
 * 
 * @author Oscar Stigter
 */
@Entity(name = "USER")
public class User implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 7032910103293449817L;

    /** ID. */
    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private long id;

    /** Username. */
    @Basic
    @Column(name = "NAME", nullable = false, unique = true)
    private String username;

    /** Password. */
    @Basic
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    
    /** Projects. */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USER_ID")
    private Set<Project> projects;

    /**
     * Returns the ID.
     * 
     * @return The ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID.
     * 
     * @param id
     *            The ID.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the username.
     * 
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * 
     * @param username
     *            The username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password.
     * 
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * 
     * @param password
     *            The password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the projects.
     * 
     * @return The projects.
     */
    public Set<Project> getProjects() {
        return Collections.unmodifiableSet(projects);
    }

    public void addProject(Project project) {
        if (projects == null) {
            projects = new TreeSet<Project>();
        }
        projects.add(project);
    }
    
    public void removeProject(Project project) {
        projects.remove(project);
        if (projects.size() == 0) {
            projects = null;
        }
    }

}
