package org.ozsoft.xmldb;

/**
 * Abstract base class for all node types.
 * 
 * @author Oscar Stigter
 */
public abstract class Node {
    
    protected String name;
    
    /* package */ Node(String name) {
        setName(name);
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String getText();
    
    public abstract String toXml();
    
    /* package */ void setName(String name) {
        this.name = name;
    }
    
    /* package */ abstract String toXml(int indent);
    
    /* package */ static String getIndent(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

}
