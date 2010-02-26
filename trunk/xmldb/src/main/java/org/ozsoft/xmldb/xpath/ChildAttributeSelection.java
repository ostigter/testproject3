package org.ozsoft.xmldb.xpath;

import java.util.ArrayList;
import java.util.List;

import org.ozsoft.xmldb.Attribute;
import org.ozsoft.xmldb.Element;
import org.ozsoft.xmldb.Node;

public class ChildAttributeSelection extends NodeSelection {
	
	private final String name;
	
	public ChildAttributeSelection(String name) {
		this.name = name;
	}
	
	@Override
	public List<? extends Node> evaluate(Object context) {
        if (context instanceof Element) {
            List<Attribute> attributes = new ArrayList<Attribute>();
            Element element = (Element) context;
            Attribute attribute = element.getAttribute(name);
            if (attribute != null) {
            	attributes.add(attribute);
            }
            return attributes;
        } else {
            throw new IllegalArgumentException("Invalid context (only Document or Element node)");
        }
	}

	@Override
	public String toString() {
		return "/@" + name;
	}

}
