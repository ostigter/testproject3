package org.ozsoft.jpa.entities;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.Session;
import org.ozsoft.jpa.services.PersistenceService;

/**
 * Document entity.
 * 
 * @author Oscar Stigter
 */
@Entity
@Table(name = "DOCUMENT")
public class Document implements Serializable {
    
    /** Serial version UID. */
    private static final long serialVersionUID = -3405704220042009756L;

    /** ID. */
    @Id
    @GeneratedValue
    @Column(name = "DOC_ID")
    private long id;
    
    /** Name. */
    @Basic
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;
    
    /** Content. */
    @Lob
    @Column(name = "CONTENT")
    private Blob content;

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

    public InputStream getContent() throws SQLException {
        InputStream is = null;
        if (content != null) {
            is = content.getBinaryStream();
        }
        return is;
    }
    
    public void setContent(InputStream is) throws IOException {
        Session session = PersistenceService.getSession();
        content = session.getLobHelper().createBlob(is, is.available());
    }

}
