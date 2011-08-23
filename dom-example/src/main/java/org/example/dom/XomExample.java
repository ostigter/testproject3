package org.example.dom;

import java.io.File;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.XPathContext;

/**
 * Example application with XOM.
 * 
 * @author Oscar Stigter
 */
public class XomExample {

    private static final File DOCS_DIR = new File("src/test/resources/docs");

    public static void main(String[] args) throws Exception {
        File file = new File(DOCS_DIR, "foo-001.xml");
        Builder builder = new Builder();
        Document doc = builder.build(file);
        System.out.println(doc.toXML());
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
    }

}
