package org.ozsoft.librarian.entities;

import java.util.Map;

public class Document {
    
    private long id;
    
    private User owner;
    
    private Group group;
    
    private Map<Integer, DocumentVersion> versions;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setVersions(Map<Integer, DocumentVersion> versions) {
        this.versions = versions;
    }

    public Map<Integer, DocumentVersion> getVersions() {
        return versions;
    }

}
