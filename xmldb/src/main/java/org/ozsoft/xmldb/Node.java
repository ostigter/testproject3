package org.ozsoft.xmldb;

/**
 * Abstract base class for all node types.
 * 
 * @author Oscar Stigter
 */
public abstract class Node {
    
    protected static final int INDENT = 4;
    
    protected final String name;
    
    /* package */ Node(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String getText();
    
    public abstract String toXml();
    
    /* package */ abstract String toXml(int indent);
    
    /* package */ String getIndent(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

}
