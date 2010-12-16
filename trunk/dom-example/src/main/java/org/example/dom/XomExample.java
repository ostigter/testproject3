package org.example.dom;

import java.io.File;
import java.io.IOException;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.ValidityException;
import nu.xom.XPathContext;

/**
 * Example application with XOM.
 * 
 * @author Oscar Stigter
 */
public class XomExample {
    
    private static final File DOCS_DIR = new File("src/test/resources/docs");
    
    public static void main(String[] args) {
        File file = new File(DOCS_DIR, "foo-001.xml");
        Builder builder = new Builder();
        try {
            Document doc = builder.build(file);
            String content = doc.toXML();
            System.out.println(content);
            XPathContext xpc = new XPathContext();
            xpc.addNamespace("foo", "http://www.example.org/foo");
            xpc.addNamespace("gen", "http://www.example.org/generic");
            Nodes nodes = doc.query("/foo:document/gen:header/gen:id", xpc);
            if (nodes.size() > 0) {
                String id = nodes.get(0).getValue();
                System.out.println("ID: " + id);
            } else {
                System.err.println("ID not found");
            }
        } catch (ValidityException e) {
            System.err.println("ERROR: Invalid document: " + e.getMessage());
        } catch (ParsingException e) {
            System.err.println("ERROR: Could not parse XML document: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("ERROR: Could not read file: " + e.getMessage());
        }
    }

}
