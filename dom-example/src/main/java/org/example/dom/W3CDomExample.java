package org.example.dom;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.example.dom.util.SimpleNamespaceContext;
import org.w3c.dom.Document;

/**
 * Example application with W3C DOM (JDK default).
 * 
 * @author Oscar Stigter
 */
public class W3CDomExample {

    private static final File DOCS_DIR = new File("src/test/resources/docs");

    public static void main(String[] args) {
        File file = new File(DOCS_DIR, "foo-001.xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try {
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("indent", "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(System.out));
            XPath xpath = XPathFactory.newInstance().newXPath();
            SimpleNamespaceContext nc = new SimpleNamespaceContext();
            nc.addNamespace("foo", "http://www.example.org/foo");
            nc.addNamespace("gen", "http://www.example.org/generic");
            xpath.setNamespaceContext(nc);
            XPathExpression xpe = xpath.compile("/foo:document/gen:header/gen:id");
            String id = (String) xpe.evaluate(doc, XPathConstants.STRING);
            if (!id.isEmpty()) {
                System.out.println("ID: " + id);
            } else {
                System.err.println("ID not found");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
