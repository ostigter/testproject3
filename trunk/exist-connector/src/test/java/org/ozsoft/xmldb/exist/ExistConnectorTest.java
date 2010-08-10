package org.ozsoft.xmldb.exist;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.junit.Test;
import org.ozsoft.xmldb.Collection;
import org.ozsoft.xmldb.XmldbConnector;
import org.ozsoft.xmldb.XmldbException;

/**
 * Test suite for the ExistConnector.
 * 
 * @author Oscar Stigter
 */
public class ExistConnectorTest {
    
    /** Hostname of the machine running the eXist instance. */
    private static final String HOST = "localhost";

    /** Port number of the eXist instance. */
    private static final int PORT = 8080;
    
    /** Username of the eXist user account. */
    private static final String USERNAME = "guest";
    
    /** Password of the eXist user account. */
    private static final String PASSWORD = "";

    /** Directory with the test resources. */
    private static final File RESOURCES_DIR = new File("src/test/resources");

    /** Directory with the test resources. */
    private static final File EXPORT_DIR = new File("target/test/export");

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(ExistConnectorTest.class);

    /**
     * Tests all methods of the ExistConnector.
     */
    @Test
    public void test() {
        LOG.info("Started");
        
        XmldbConnector connector = new ExistConnector(HOST, PORT, USERNAME, PASSWORD);

        try {
            String uri = null;
            Document doc = null;
            Collection col = null;
            byte[] content = null;
            String query = null;
            String result = null;
            
            LOG.debug("Importing database");
            connector.importCollection("/db", new File(RESOURCES_DIR, "/db"));

            connector.importResource("/db/modules/greeter2.xql", new File(RESOURCES_DIR, "/db/modules/greeter2.xql"));
//            connector.exportResource("/db/modules/greeter2.xql", new File(EXPORT_DIR,    "/db/modules/greeter2.xql"));
//            
//            LOG.debug("Exporting non-collection resources");
//            connector.exportResource("/db/data/bar/bar-002.xml", new File(EXPORT_DIR, "db/data/bar/bar-002.xml"));
//            connector.exportResource("/db/modules/greeter1.xql", new File(EXPORT_DIR, "greeter1.xql"));
//
//            LOG.debug("Exporting collections");
//            connector.exportCollection("/db/modules", new File(EXPORT_DIR, "/db/modules"));

            LOG.debug("Listing the collection '/db/data':");
            col = connector.retrieveCollection("/db/data");
            Assert.assertNotNull(col);
            Assert.assertEquals(2, col.getResources().size());
            Assert.assertEquals("bar", col.getResources().get(0).getName());
            Assert.assertTrue(col.getResources().get(0) instanceof Collection);
            Assert.assertEquals("foo", col.getResources().get(1).getName());
            Assert.assertTrue(col.getResources().get(1) instanceof Collection);

            LOG.debug("Listing the collection '/db/data/foo':");
            col = connector.retrieveCollection("/db/data/foo");
            Assert.assertNotNull(col);
            Assert.assertEquals(2, col.getResources().size());
            Assert.assertEquals("foo-001.xml", col.getResources().get(0).getName());
            Assert.assertFalse(col.getResources().get(0) instanceof Collection);
            Assert.assertEquals("foo-002.xml", col.getResources().get(1).getName());
            Assert.assertFalse(col.getResources().get(1) instanceof Collection);

            LOG.debug("Retrieving the raw content of a resource");
            uri = "/db/data/foo/foo-001.xml";
            content = connector.retrieveResource(uri);
            LOG.debug("Resource content:\n" + new String(content));
            Assert.assertNotNull(content);
            Assert.assertEquals(64, content.length);

            LOG.debug("Retrieving the content of an XML document");
            uri = "/db/data/foo/foo-001.xml";
            doc = connector.retrieveXmlDocument(uri);
            if (doc == null) {
                Assert.fail("Document not found: " + uri);
            }
            result = doc.asXML();
            LOG.debug("XML document content:\n" + result);
            Assert.assertTrue(result.contains("<type>Foo</type>"));
            Assert.assertTrue(result.contains("<id>foo-001</id>"));

            LOG.debug("Executing an standalone, ad-hoc query");
            query = "for $x in (1, 2, 3) return <number>{$x}</number>";
            result = connector.executeQuery(query);
            LOG.debug("Result:\n" + result);
            Assert.assertTrue(result.contains("<number>1</number>"));
            Assert.assertTrue(result.contains("<number>2</number>"));
            Assert.assertTrue(result.contains("<number>3</number>"));

            LOG.debug("Executing an ad-hoc query over documents");
            query = "for $doc in collection('/db/data/foo') return <id>{$doc/document/id/text()}</id>";
            result = connector.executeQuery(query);
            LOG.debug("Result:\n" + result);
            Assert.assertTrue(result.contains("<id>foo-001</id>"));
            Assert.assertTrue(result.contains("<id>foo-002</id>"));

            LOG.debug("Calling an executable XQuery module");
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("name", "Mr. Jackson");
            result = connector.callModule("/db/modules/greeter1.xql", paramMap);
            LOG.debug("Result:\n" + result);
            Assert.assertEquals("<Greeting>Hello, Mr. Jackson!</Greeting>", result);

            LOG.debug("Calling an XQuery library function");
            String[] params = new String[] { "Mr. Smith" };
            result = connector.callFunction("http://www.example.org/greeter2", "/db/modules/greeter2.xql", "greeting", params);
            LOG.debug("Result:\n" + result);
            Assert.assertTrue(result.contains("<Greeting>Hello, Mr. Smith!</Greeting>"));
            
            LOG.debug("Deleting resources");
            connector.deleteResource("/db/data");
            Assert.assertNull(connector.retrieveCollection("/db/data"));
            connector.deleteResource("/db/modules");
            Assert.assertNull(connector.retrieveCollection("/db/modules"));
            
            LOG.info("Finished");

        } catch (XmldbException e) {
            LOG.error("ERROR: " + e.getMessage(), e);
        }
    }

}
