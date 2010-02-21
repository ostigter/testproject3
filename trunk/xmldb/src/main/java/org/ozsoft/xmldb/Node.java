package org.ozsoft.xmldb;

public abstract class Node {
    
    private final String name;
    
    /* package */ Node(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public abstract String getText();

}
