package org.ozsoft.xmldb.xpath;

import java.util.ArrayList;
import java.util.List;

import org.ozsoft.xmldb.Document;
import org.ozsoft.xmldb.Element;

public class ChildElement implements Expression {
    
    @Override
    public Object evaluate(Object context) {
        List<Element> elements = new ArrayList<Element>();
        if (context instanceof Document) {
            Document doc = (Document) context;
            Element element = doc.getRootElement();
            if (element != null) {
                elements.add(element);
            }
        } else if (context instanceof Element) {
            Element element = (Element) context;
            for (Element child : element.getElements()) {
                elements.add(child);
            }
        } else {
            throw new IllegalArgumentException("Invalid context (only Document or Element node)");
        }
        return elements;
    }

}
