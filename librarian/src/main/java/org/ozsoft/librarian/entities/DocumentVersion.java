package org.ozsoft.librarian.entities;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class DocumentVersion extends BaseEntity {
    
    private static final long serialVersionUID = -5157261270180628917L;

    @Basic
    private int versionNumber;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getVersionNumber() {
        return versionNumber;
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
