package org.ozsoft.xmldb;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Element extends Node {
    
    private Map<String, Attribute> attributes;
    
    private List<Node> nodes;
    
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
        addNode(element);
        return element;
    }
    
    public Element addElement(String name, String text) {
        Element element = addElement(name);
        element.setText(text);
        return element;
    }
    
    /* package */ void addNode(Node node) {
        if (nodes == null) {
            nodes = new ArrayList<Node>();
        }
        nodes.add(node);
    }
    
    /* package */ void addElement(Element element) {
        addNode(element);
    }
    
    /* package */ void addText(Text text) {
        addNode(text);
    }
    
    public List<Node> getNodes() {
        return nodes;
    }
    
    public List<Element> getElements() {
        List<Element> elements = new ArrayList<Element>();
        if (nodes != null) {
            for (Node node : nodes) {
                if (node instanceof Element) {
                    elements.add((Element) node);
                }
            }
        }
        return elements;
    }
    
    public Element getElement(int index) {
        if (nodes != null) {
            int i = 0;
            for (Node node : nodes) {
                if (node instanceof Element) {
                    i++;
                    if (i == index) {
                        return (Element) node;
                    }
                }
            }
        }
        return null;
    }
    
    public Element getElement(String name) {
        if (nodes != null) {
            for (Node node : nodes) {
                if (node instanceof Element && node.getName().equals(name)) {
                    return (Element) node;
                }
            }
        }
        return null;
    }
    
    public List<Element> getElements(String name) {
        List<Element> elements = new ArrayList<Element>();
        if (nodes != null) {
            for (Node node : nodes) {
                if (node instanceof Element && node.getName().equals(name)) {
                    elements.add((Element) node);
                }
            }
        }
        return elements;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        if (nodes != null) {
            for (Node node : nodes) {
                sb.append(node.getText());
            }
        }
        return sb.toString();
    }
    
    public void setText(String text) {
        if (nodes != null) {
            nodes.clear();
        }
        addNode(new Text(text));
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
        if (nodes == null) {
            sb.append(" /");
        }
        sb.append('>');
        if (nodes != null) {
            boolean lastIsElement = false;
            for (Node node : nodes) {
                if (node instanceof Element) {
                    sb.append('\n');
                    sb.append(node.toXml(indent + INDENT));
                    lastIsElement = true;
                } else {
                    sb.append(node.toXml());
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
