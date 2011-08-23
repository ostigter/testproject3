package org.example.dom;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * Example application with DOM4j.
 * 
 * @author Oscar Stigter
 */
public class Dom4jExample {

    private static final File DOCS_DIR = new File("src/test/resources/docs");

    public static void main(String[] args) throws Exception {
        File file = new File(DOCS_DIR, "foo-001.xml");
        SAXReader reader = new SAXReader();
        Document doc = reader.read(file);
        XMLWriter writer = new XMLWriter(System.out);
        writer.write(doc);
        System.out.println();
        XPath xpath = DocumentHelper.createXPath("/foo:document/gen:header/gen:id");
        org.jaxen.SimpleNamespaceContext nc = new org.jaxen.SimpleNamespaceContext();
        nc.addNamespace("foo", "http://www.example.org/foo");
        nc.addNamespace("gen", "http://www.example.org/generic");
        xpath.setNamespaceContext(nc);
        Element element = (Element) xpath.selectSingleNode(doc);
        if (element != null) {
            String id = element.getText();
            System.out.println("ID: " + id);
        } else {
            System.err.println("ID not found");
        }
    }

}
