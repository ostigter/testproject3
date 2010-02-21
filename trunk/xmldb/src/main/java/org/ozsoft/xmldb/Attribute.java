package org.ozsoft.xmldb;

public class Attribute extends Node {
    
    private String value;

    public Attribute(String name, String value) {
        super(name);
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String getText() {
        return value;
    }

}