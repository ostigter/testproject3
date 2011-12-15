package org.ozsoft.projectbase.entities;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Project extends BaseEntity {
    
    private static final long serialVersionUID = -3674896318075447666L;
    
    @Basic
    private String name;

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
