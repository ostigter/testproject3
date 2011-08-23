package org.example.dom;

import java.io.File;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * Example application with JDOM.
 * 
 * @author Oscar Stigter
 */
public class JDomExample {

    private static final File DOCS_DIR = new File("src/test/resources/docs");

    public static void main(String[] args) throws Exception {
        File file = new File(DOCS_DIR, "foo-001.xml");
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(file);
        Format format = Format.getPrettyFormat();
        format.setIndent("  ");
        XMLOutputter outputter = new XMLOutputter(format);
        outputter.output(doc, System.out);
        XPath xpath = XPath.newInstance("/foo:document/gen:header/gen:id");
        xpath.addNamespace("foo", "http://www.example.org/foo");
        xpath.addNamespace("gen", "http://www.example.org/generic");
        Element element = (Element) xpath.selectSingleNode(doc);
        if (element != null) {
            String id = element.getText();
            System.out.println("ID: " + id);
        } else {
            System.err.println("ID not found");
        }
    }

}
