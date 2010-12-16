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
        long startTime = System.currentTimeMillis();
        SAXBuilder builder = new SAXBuilder();
        long duration = System.currentTimeMillis() - startTime;
        System.out.format("Initialized in %d ms\n", duration);
        try {
            startTime = System.currentTimeMillis();
            Document doc = builder.build(file);
            duration = System.currentTimeMillis() - startTime;
            System.out.format("Document read in %d ms\n", duration);
            Format format = Format.getPrettyFormat();
            format.setIndent("    ");
            XMLOutputter outputter = new XMLOutputter(format);
            StringWriter sw = new StringWriter();
            startTime = System.currentTimeMillis();
            outputter.output(doc, sw);
            duration = System.currentTimeMillis() - startTime;
            System.out.format("Document written in %d ms\n", duration);
            sw.close();
            startTime = System.currentTimeMillis();
            XPath xpath = XPath.newInstance("/foo:document/gen:header/gen:id");
            xpath.addNamespace("foo", "http://www.example.org/foo");
            xpath.addNamespace("gen", "http://www.example.org/generic");
            duration = System.currentTimeMillis() - startTime;
            System.out.format("XPath prepared in in %d ms\n", duration);
            startTime = System.currentTimeMillis();
            Element element = (Element) xpath.selectSingleNode(doc);
            duration = System.currentTimeMillis() - startTime;
            System.out.format("XPath evaluated in in %d ms\n", duration);
            if (element != null) {
                String id = element.getText();
                System.out.println("ID: " + id);
            } else {
                System.err.println("ID not found");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
