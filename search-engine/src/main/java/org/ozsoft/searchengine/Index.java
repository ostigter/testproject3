package org.ozsoft.searchengine;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Index {
    
    @Id
    @GeneratedValue
    private long id;
    
    @Basic
    private Item item;
    
    @Basic
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
