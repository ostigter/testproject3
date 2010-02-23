package org.example.dom;

import java.io.File;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
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
    
    public static void main(String[] args) {
        File file = new File(DOCS_DIR, "foo-001.xml");
        SAXReader reader = new SAXReader();
        try {
            Document doc = reader.read(file);
            StringWriter sw = new StringWriter();
            XMLWriter writer = new XMLWriter(sw);
            writer.write(doc);
            String content = sw.toString();
            sw.close();
            System.out.println(content);
            XPath xpath = DocumentHelper.createXPath("/foo:document/gen:header/gen:id");
            org.jaxen.SimpleNamespaceContext nc = new org.jaxen.SimpleNamespaceContext();
            nc.addNamespace("foo", "http://www.example.org/foo");
            nc.addNamespace("gen", "http://www.example.org/generic");
            xpath.setNamespaceContext(nc);
            Node node = xpath.selectSingleNode(doc);
            String id = null;
            if (node != null) {
                id = node.getText();
            }
            System.out.println("ID: " + id);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
