package org.ozsoft.searchengine;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "findAllItems", query = "SELECT item FROM Item item")
public class Item implements Serializable {
    
    private static final long serialVersionUID = 5253607267211299872L;

    @Id
    @GeneratedValue
    private long id;
    
    @Basic
    private String name;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
