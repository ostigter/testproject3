package org.ozsoft.xmldb;

/**
 * A text node.
 * 
 * @author Oscar Stigter
 */
public class Text extends Node {
    
    private String value;
    
    /* package */ Text(Database database, String value) {
        super(database);
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
    public String toString() {
        return String.format("Text(\"%s\")", value);
    }
    
    @Override
    /* package */ String toXml(int indent) {
        return getIndent(indent) + value;
    }
    
}
