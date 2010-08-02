package org.ozsoft.xmldb.test;

import java.io.File;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.xmldb.Database;
import org.ozsoft.xmldb.Document;
import org.ozsoft.xmldb.DocumentParser;
import org.ozsoft.xmldb.Element;
import org.ozsoft.xmldb.Node;
import org.ozsoft.xmldb.xpath.ChildElementSelection;
import org.ozsoft.xmldb.xpath.NodeSelection;

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
    public void parserSmall() {
        Database database = new Database();
        DocumentParser parser = database.createParser();
        Document doc = parser.parse(new File(DOCS_DIR, "foo-001.xml"));
        Element rootElement = doc.getRootElement();
        Assert.assertNotNull(rootElement);
        Assert.assertEquals("document", rootElement.getName());
    }
    
    @Test
    public void parserLarge() {
        showMemoryUsage();
        Database database = new Database();
        DocumentParser parser = database.createParser();
        Document doc = parser.parse(new File(DOCS_DIR, "mondial.xml"));
        showMemoryUsage();
        Element rootElement = doc.getRootElement();
        Assert.assertNotNull(rootElement);
        Assert.assertEquals("mondial", rootElement.getName());
        NodeSelection nodeExpr = new NodeSelection();
        nodeExpr.add(new ChildElementSelection("mondial"));
        nodeExpr.add(new ChildElementSelection("country"));
        nodeExpr.add(new ChildElementSelection("name"));
        List<? extends Node> countryElements = nodeExpr.evaluate(doc);
        Assert.assertNotNull(countryElements);
        Assert.assertEquals(238, countryElements.size());
    }

    private static void showMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        double used = ((double) (runtime.totalMemory() - runtime.freeMemory())) / (1024 * 1024);
        System.out.format(Locale.US, "Memory usage:  %.2f MB\n", used);
    }

}
