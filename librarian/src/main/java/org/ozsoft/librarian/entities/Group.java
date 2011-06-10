package org.ozsoft.librarian.entities;

public class Group {
    
    private long id;
    
    private String name;
    
    private User groupLead;

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

    public void setGroupLead(User groupLead) {
        this.groupLead = groupLead;
    }

    public User getGroupLead() {
        return groupLead;
    }
    
}
