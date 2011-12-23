package org.ozsoft.jboss.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    
    @Override
    public String toString() {
        return name;
    }

}
