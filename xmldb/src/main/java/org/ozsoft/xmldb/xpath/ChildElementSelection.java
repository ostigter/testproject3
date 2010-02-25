package org.ozsoft.xmldb.xpath;

import java.util.ArrayList;
import java.util.List;

import org.ozsoft.xmldb.Document;
import org.ozsoft.xmldb.Element;

public class ChildElementSelection extends NodeSelection {
	
	private final String name;
	
	public ChildElementSelection(String name) {
		this.name = name;
	}
	
	@Override
	public List<?> evaluate(Object context) {
        List<Element> elements = new ArrayList<Element>();
        if (context instanceof Document) {
            Document doc = (Document) context;
            Element element = doc.getRootElement();
            if (element != null && element.getName().equals(name)) {
                elements.add(element);
            }
        } else if (context instanceof Element) {
            elements.addAll(((Element) context).getElements(name));
        } else {
            throw new IllegalArgumentException("Invalid context (only Document or Element node)");
        }
        return elements;
	}

}
