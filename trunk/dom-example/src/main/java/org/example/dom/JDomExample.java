package org.example.dom;

import java.io.File;
import java.io.StringWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * Example application with DOM4j.
 * 
 * @author Oscar Stigter
 */
public class JDomExample {
    
    private static final File DOCS_DIR = new File("src/test/resources/docs");
    
    public static void main(String[] args) {
        File file = new File(DOCS_DIR, "foo-001.xml");
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(file);
            Format format = Format.getPrettyFormat();
            format.setIndent("    ");
            XMLOutputter outputter = new XMLOutputter(format);
            StringWriter sw = new StringWriter();
            outputter.output(doc, sw);
            sw.close();
            String content = sw.toString();
            System.out.println(content);
            XPath xpath = XPath.newInstance("/foo:document/gen:header/gen:id");
            xpath.addNamespace("foo", "http://www.example.org/foo");
            xpath.addNamespace("gen", "http://www.example.org/generic");
            Element element = (Element) xpath.selectSingleNode(doc);
            String id = null;
            if (element != null) {
                id = element.getText();
            }
            System.out.println("ID: " + id);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
