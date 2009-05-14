package org.ozsoft.fondsbeheer.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "findFunds", query = "SELECT f FROM Fund f"),
    @NamedQuery(name = "findFundsByCategory", query = "SELECT f FROM Fund f WHERE f.categoryId = :categoryId")
})
public class Fund {
    
    @Id
    private String id;
    
    private String name;
    
    private String categoryId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
}
