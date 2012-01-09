package org.ozsoft.jboss.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Project implements Serializable {
    
    private static final long serialVersionUID = -2334607643073889386L;

    @Id
    @GeneratedValue
    private Long id;
    
    @Basic
    private String code;
    
    @Basic
    private String name;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Release> releases;
    
    public Project() {
    	releases = new ArrayList<Release>();
    }
    
    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<Release> getReleases() {
    	return releases;
    }
    
    @Override
    public String toString() {
        return name;
    }

}
