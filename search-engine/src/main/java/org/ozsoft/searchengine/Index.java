package org.ozsoft.searchengine;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "findIndex", query = "SELECT index FROM Index index WHERE index.tag = ?1")
public class Index implements Serializable {
    
    private static final long serialVersionUID = -3450064557416055434L;

    @Id
    @GeneratedValue
    private long id;
    
    private Item item;
    
    private Tag tag;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }

}
