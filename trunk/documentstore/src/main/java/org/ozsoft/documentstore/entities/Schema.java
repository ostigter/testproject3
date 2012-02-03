package org.ozsoft.documentstore.entities;

import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Entity
public class Schema extends BaseEntity {

    private static final long serialVersionUID = -8448681026062862451L;
    
    @Basic
    private String namespace;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private Blob content;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Blob getContent() {
        return content;
    }

    public void setContent(Blob content) {
        this.content = content;
    }

}
