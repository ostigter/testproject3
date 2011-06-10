package org.ozsoft.librarian.entities;

import java.util.Date;

public class DocumentVersion {
    
    private long id;
    
    private int versionNumber;
    
    private User author;
    
    private Date creationDate;
    
    private Date modificationDate;

    private DocumentStatus status;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getAuthor() {
        return author;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public DocumentStatus getStatus() {
        return status;
    }
    
}
