package org.ozsoft.xmldb.test;

import java.io.File;

import org.junit.Test;
import org.ozsoft.xmldb.Database;
import org.ozsoft.xmldb.Document;
import org.ozsoft.xmldb.Element;
import org.ozsoft.xmldb.Parser;

public class XmldbTest {
    
    private static final File DOCS_DIR = new File("src/test/resources/docs");
    
    @Test
    public void manualBuild() {
        Database database = new Database();
        Document doc = database.createDocument("foo-001.xml");
        Element rootElement = doc.addRootElement("document");
        rootElement.addAttribute("lang", "EN");
        Element headerElement = rootElement.addElement("header");
        headerElement.addElement("id").setText("foo-001");
        headerElement.addElement("type").setText("Foo");
        Element bodyElement = rootElement.addElement("body");
        bodyElement.addElement("title").setText("Some Foo document");
        bodyElement.addElement("author").setText("John Doe");
    }
    
    @Test
    public void parser() {
        Database database = new Database();
        Parser parser = database.createParser();
        Document doc = parser.parse(new File(DOCS_DIR, "foo-001.xml"));
        System.out.println(doc.toXml());
    }

}
