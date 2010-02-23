package org.ozsoft.xmldb;

/**
 * An attribute node.
 * 
 * @author Oscar Stigter
 */
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
    
    @Override
    public String toXml() {
        return String.format("%s=\"%s\"", getName(), value);
    }

    @Override
    public String toString() {
        return toXml();
    }
    
    @Override
    /* package */ String toXml(int indent) {
        return toXml();
    }

}
