package org.ozsoft.jpa.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity(name = "DOCUMENT")
public class Document implements Serializable {
    
    /** Serial version UID. */
    private static final long serialVersionUID = -3405704220042009756L;

    /** ID. */
    @Id
    @GeneratedValue
    private long id;
    
    /** Name. */
    @Basic
    private String name;
    
    /** Content. */
    @Lob
    private byte[] content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }
    
    public void setContent(byte[] content) {
        this.content = content;
    }

}
