package org.ozsoft.projectbase.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Project extends BaseEntity {

    private static final long serialVersionUID = -3674896318075447666L;

    @Basic
    protected String name;

    @Basic
    private String code;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Release> releases;
    
    public Project() {
    	releases = new ArrayList<Release>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }
    
    @Override
    public String toString() {
        return name;
    }

}
