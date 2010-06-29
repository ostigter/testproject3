package org.ozsoft.xmldb;

import java.util.HashMap;
import java.util.Map;

public class Database {
    
    private final Map<Long, Node> nodes;
    
    private final Collection rootCollection;
    
    private long nextId = 1;
    
    public Database() {
        nodes = new HashMap<Long, Node>(); 
        rootCollection = new Collection(this);
        rootCollection.setName("");
        addNode(rootCollection);
    }
    
    public Collection getRootCollection() {
        return rootCollection;
    }
    
    public Parser createParser() {
        return new Parser(this);
    }
    
    public Document createDocument() {
        return new Document(this);
    }
    
    public Document createDocument(String name) {
        Document doc = createDocument();
        doc.setName(name);
        return doc;
    }
    
    public Element createElement(String name) {
        Element element = new Element(this, name);
        return element;
    }
    
    public Attribute createAttribute(String name, String value) {
        return new Attribute(this, name, value);
    }
    
    public Text createText(String value) {
        return new Text(this, value);
    }
    
    /* package */ long getNextId() {
        return nextId++;
    }
    
    /* package */ void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

}
