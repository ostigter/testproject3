package org.ozsoft.xmldb;

public class Database {
    
    private final Collection rootCollection;
    
    private long nextId = 1;
    
    public Database() {
        rootCollection = new Collection("");
    }
    
    public Collection getRootCollection() {
        return rootCollection;
    }
    
    /* package */ long getNextId() {
        return nextId++;
    }

}
