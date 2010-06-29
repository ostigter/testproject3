package org.ozsoft.xmldb;

/**
 * A document node.
 * 
 * @author Oscar Stigter
 */
public class Document extends Resource {
    
    private Element rootElement;
    
    /* package */ Document(Database database) {
        super(database);
    }
    
    public Element getRootElement() {
        return rootElement;
    }
    
    public void setRootElement(Element rootElement) {
        this.rootElement = rootElement;
    }

    public Element addRootElement(String name) {
        if (rootElement != null) {
            throw new IllegalStateException("Root element already set");
        }
        Element element = new Element(database, name);
        setRootElement(element);
        return element;
    }

    @Override
    public String getText() {
        return rootElement.getText();
    }

    @Override
    public String toXml() {
        return rootElement.toXml();
    }
    
    @Override
    public String toString() {
        return String.format("Document(\"%s\")", name);
    }
    
    @Override
    /* package */ String toXml(int indent) {
        return toXml();
    }
    
}
