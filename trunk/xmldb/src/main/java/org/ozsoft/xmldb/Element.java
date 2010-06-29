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
    
    private static final int INDENT = 4;
    
    private Map<String, Attribute> attributes;
    
    private List<Node> children;
    
    /* package */ Element(Database database, String name) {
        super(database);
        setName(name);
    }
    
    public List<Attribute> getAttributes() {
    	List<Attribute> list = new ArrayList<Attribute>();
    	list.addAll(attributes.values());
    	return list;
    }
    
    public Attribute getAttribute(String name) {
    	return (attributes != null) ? attributes.get(name) : null;
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
    
    public void addAttribute(String name, String value) {
        if (attributes == null) {
            attributes = new LinkedHashMap<String, Attribute>();
        }
        attributes.put(name, new Attribute(database, name, value));
    }
    
    /* package */ void addChild(Node child) {
        if (children == null) {
            children = new ArrayList<Node>();
        }
        children.add(child);
    }
    
    public Element addElement(String name) {
        Element element = new Element(database, name);
        addElement(element);
        return element;
    }
    
    public Element addElement(String name, String value) {
        Element element = new Element(database, name);
        Text text = new Text(database, value);
        element.addText(text);
        addElement(element);
        return element;
    }
    
    /* package */ void addElement(Element element) {
        addChild(element);
    }
    
    /* package */ void addText(Text text) {
        addChild(text);
    }
    
    public List<Node> getChildren() {
        return children;
    }
    
    public Node getChild(int index) {
    	return children.get(index);
    }
    
    public List<Element> getElements() {
        List<Element> result = new ArrayList<Element>();
        if (children != null) {
            for (Node child : children) {
                if (child instanceof Element) {
                    result.add((Element) child);
                }
            }
        }
        return result;
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
    
    public void setText(String value) {
        if (children != null) {
            children.clear();
        }
        Text text = new Text(database, value);
        addChild(text);
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
