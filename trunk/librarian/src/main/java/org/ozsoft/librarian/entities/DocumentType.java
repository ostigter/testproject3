package org.ozsoft.librarian.entities;

public class DocumentType {
    
    private long id;
    
    private String code;
    
    private String name;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
