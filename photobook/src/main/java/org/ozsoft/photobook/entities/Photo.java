package org.ozsoft.photobook.entities;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Photo extends BaseEntity {

    @Lob
    private Blob content;

    protected Blob getContent() {
        return content;
    }

    protected void setContent(Blob content) {
        this.content = content;
    }

}
