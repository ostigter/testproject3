package org.ozsoft.photobook.entities;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class Photo extends BaseEntity {

    private static final long serialVersionUID = -3625810657812632685L;

    @Basic
    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
