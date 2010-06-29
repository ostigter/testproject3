package org.ozsoft.xmldb;

/**
 * Abstract base class for all node types.
 * 
 * @author Oscar Stigter
 */
public abstract class Node {
    
    protected final Database database;
    
    protected final long id;
    
    protected String name;
    
    /* package */ Node(Database database) {
        this.database = database;
        id = database.getNextId();
        database.addNode(this);
    }
    
    /* package */ long getId() {
        return id;
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
