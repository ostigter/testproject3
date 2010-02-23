package org.ozsoft.xmldb;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Element node.
 * 
 * @author Oscar Stigter
 */
public class Element extends Node {
    
    private Map<String, Attribute> attributes;
    
    private List<Node> children;
    
    /* package */ Element(String name) {
        super(name);
    }
    
    public void addAttribute(String name, String value) {
        if (attributes == null) {
            attributes = new LinkedHashMap<String, Attribute>();
        }
        attributes.put(name, new Attribute(name, value));
    }
    
    public Attribute getAttribute(String name) {
        return attributes.get(name);
    }
    
    public String getAttributeValue(String name) {
        Attribute attribute = getAttribute(name);
        String value = null;
        if (attribute != null) {
            value = attribute.getValue();
        }
        return value;
    }
    
    public void removeAttribute(String name) {
        attributes.remove(name);
    }
    
    public Element addElement(String name) {
        Element element = new Element(name);
        addChild(element);
        return element;
    }
    
    public Element addElement(String name, String text) {
        Element element = addElement(name);
        element.setText(text);
        return element;
    }
    
    /* package */ void addChild(Node child) {
        if (children == null) {
            children = new ArrayList<Node>();
        }
        children.add(child);
    }
    
    /* package */ void addElement(Element element) {
        addChild(element);
    }
    
    /* package */ void addText(Text text) {
        addChild(text);
    }
    
    public List<Node> getNodes() {
        return children;
    }
    
    public List<Element> getElements() {
        List<Element> elements = new ArrayList<Element>();
        if (children != null) {
            for (Node child : children) {
                if (child instanceof Element) {
                    elements.add((Element) child);
                }
            }
        }
        return elements;
    }
    
    public Element getElement(int index) {
        if (children != null) {
            int i = 0;
            for (Node child : children) {
                if (child instanceof Element) {
                    i++;
                    if (i == index) {
                        return (Element) child;
                    }
                }
            }
        }
        return null;
    }
    
    public Element getElement(String name) {
        if (children != null) {
            for (Node child : children) {
                if (child instanceof Element && child.getName().equals(name)) {
                    return (Element) child;
                }
            }
        }
        return null;
    }
    
    public List<Element> getElements(String name) {
        List<Element> elements = new ArrayList<Element>();
        if (children != null) {
            for (Node child : children) {
                if (child instanceof Element && child.getName().equals(name)) {
                    elements.add((Element) child);
                }
            }
        }
        return elements;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        if (children != null) {
            for (Node child : children) {
                sb.append(child.getText());
            }
        }
        return sb.toString();
    }
    
    public void setText(String text) {
        if (children != null) {
            children.clear();
        }
        addChild(new Text(text));
    }

    @Override
    public String toXml() {
        return toXml(0);
    }
    
    @Override
    public String toString() {
        return String.format("Element(\"%s\")", name);
    }
    
    /* package */ String toXml(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndent(indent));
        sb.append('<').append(name);
        if (attributes != null) {
            for (String key : attributes.keySet()) {
                String value = attributes.get(key).getValue();
                sb.append(' ');
                sb.append(key);
                sb.append("=\"");
                sb.append(value);
                sb.append("\"");
            }
        }
        if (children == null) {
            sb.append(" /");
        }
        sb.append('>');
        if (children != null) {
            boolean lastIsElement = false;
            for (Node child : children) {
                if (child instanceof Element) {
                    sb.append('\n');
                    sb.append(child.toXml(indent + INDENT));
                    lastIsElement = true;
                } else {
                    sb.append(child.toXml());
                    lastIsElement = false;
                }
            }
            if (lastIsElement) {
                sb.append('\n');
                sb.append(getIndent(indent));
            }
            sb.append("</").append(name).append('>');
        }
        return sb.toString();
    }

}
