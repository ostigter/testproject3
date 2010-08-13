package org.ozsoft.xmldb.exist;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.junit.Test;
import org.ozsoft.xmldb.Collection;
import org.ozsoft.xmldb.NotAuthorizedException;
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
    private static final String PASSWORD = "guest";

    /** Directory with the test resources. */
    private static final File RESOURCES_DIR = new File("src/test/resources");

//    /** Directory with the test resources. */
//    private static final File EXPORT_DIR = new File("target/test/export");

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(ExistConnectorTest.class);
    
    /**
     * Tests all functionality of the ExistConnector.
     */
    @Test
    public void existConnector() throws XmldbException {
        LOG.info("Test 'existConnector' started");
        
        XmldbConnector connector = new ExistConnector(HOST, PORT, USERNAME, PASSWORD);

        String uri = null;
        Document doc = null;
        Collection col = null;
        byte[] content = null;
        String query = null;
        String result = null;
        
        LOG.debug("Importing database");
        connector.importCollection("/db", new File(RESOURCES_DIR, "/db"));

        //FIXME: Disabled because of bug (see separate test).
//        LOG.debug("Exporting collections");
//        connector.exportCollection("/db/modules", new File(EXPORT_DIR, "/db/modules"));

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
        
        LOG.info("Test 'existConnector' finished");
    }

    /**
     * Test the security while accessing resources (authentication and
     * authorization).
     */
    @Test
    public void security() {
        LOG.info("Test 'security' started");
        
        // Unauthorized access (guest role).
        XmldbConnector connector = new ExistConnector(HOST, PORT, "guest", "guest");
        try {
            connector.retrieveXmlDocument("/db/system/users.xml");
            Assert.fail("No exception thrown");
        } catch (NotAuthorizedException e) {
            // OK
        } catch (XmldbException e) {
            Assert.fail("Incorrect exception type: " + e);
        }

        // Authorized access (DBA role).
        connector = new ExistConnector(HOST, PORT, "admin", "admin");
        try {
            Document doc = connector.retrieveXmlDocument("/db/system/users.xml");
            Assert.assertNotNull(doc);
        } catch (XmldbException e) {
            Assert.fail(e.getMessage());
        }

        LOG.info("Test 'security' finished");
    }
    
    /**
     * Tests a bug when calling a XQuery function of library module.
     */
    @Test
    public void bug() throws XmldbException {
        LOG.info("Test 'bug' started");
        
        XmldbConnector connector = new ExistConnector(HOST, PORT, USERNAME, PASSWORD);

        // Import an XQuery library module.
        connector.importResource("/db/modules/greeter2.xql", new File(RESOURCES_DIR, "/db/modules/greeter2.xql"));

        // Retrieve the XQuery library module through the REST interface (always returns empty result).
        //FIXME: Enabling this statement seems to cause the bug!
        byte[] content = connector.retrieveResource("/db/modules/greeter2.xql");
        Assert.assertNotNull(content);
        Assert.assertEquals(0, content.length);
        
        // If the above block is enabled, the XQuery function call will cause a HTTP 500 (Server Error).
        String[] params = new String[] { "Mr. Smith" };
        String result = connector.callFunction("http://www.example.org/greeter2", "/db/modules/greeter2.xql", "greeting", params);
        LOG.debug("Result:\n" + result);
        Assert.assertTrue(result.contains("<Greeting>Hello, Mr. Smith!</Greeting>"));

        LOG.info("Test 'bug' finished");
    }

}
