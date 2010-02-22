package org.ozsoft.xmldb;

public class Text extends Node {
    
    private String value;
    
    public Text(String value) {
        super(null);
        setValue(value);
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
    
    @Override
    public String toXml() {
        return value;
    }

    @Override
    /* package */ String toXml(int indent) {
        return getIndent(indent) + value;
    }
    
}
