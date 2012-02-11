package org.ozsoft.documentstore.entities;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class DocumentType extends BaseEntity {

    private static final long serialVersionUID = -6531760597643022931L;

    @Basic
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
