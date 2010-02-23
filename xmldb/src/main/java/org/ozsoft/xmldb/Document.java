package org.ozsoft.xmldb;

/**
 * A document node.
 * 
 * @author Oscar Stigter
 */
public class Document extends Node {
    
    private Element rootElement;
    
    public Document() {
        super(null);
    }
    
    public Document(String name) {
        super(name);
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
        Element element = new Element(name);
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
