package org.ozsoft.xmldb.test;

import org.junit.Test;
import org.ozsoft.xmldb.Document;
import org.ozsoft.xmldb.Element;

public class XmldbTest {
    
    @Test
    public void nodes() {
        Document doc = new Document("foo-001.xml");
        Element rootElement = doc.addRootElement("document");
        Element headerElement = rootElement.addElement("header");
        headerElement.addElement("id", "foo-001");
        headerElement.addElement("type", "Foo");
        Element bodyElement = rootElement.addElement("body");
        bodyElement.addElement("title", "Some Foo document");
        bodyElement.addElement("author", "John Doe");
    }

}
