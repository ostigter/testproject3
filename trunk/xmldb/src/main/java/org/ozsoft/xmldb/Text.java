package org.ozsoft.xmldb;

public class Text extends Node {
    
    private String value;
    
    public Text(String value) {
        super(null);
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
