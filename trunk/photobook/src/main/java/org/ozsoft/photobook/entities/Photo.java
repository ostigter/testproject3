package org.ozsoft.photobook.entities;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class Photo extends BaseEntity {

    @Lob
    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
