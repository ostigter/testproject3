package org.ozsoft.xmldb.exist.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.ozsoft.xmldb.Collection;
import org.ozsoft.xmldb.Resource;
import org.ozsoft.xmldb.XmldbConnector;
import org.ozsoft.xmldb.XmldbException;
import org.ozsoft.xmldb.exist.ExistConnector;

/**
 * Test driver to demonstrate the ExistRestConnector.
 * 
 * Expects a running eXist instance on the configured host and port.
 * 
 * Creates and deletes the collections '/db/data' and '/db/modules'.
 * 
 * @author Oscar Stigter
 */
public class Main {

    /** Hostname of the machine running the eXist instance. */
    private static final String HOST = "localhost";

    /** The port eXist is running on. */
    private static final int PORT = 8080;

    /** Directory with the test resources. */
    private static final File RESOURCES_DIR = new File("src/test/resources");

    /** Logger. */
    private static final Logger LOG = Logger.getLogger(Main.class);

    /**
     * Application's entry point.
     * 
     * @param args
     *            The command line arguments (not used).
     */
    public static void main(String[] args) {
        String uri = null;
        Document doc = null;
        Collection col = null;
        byte[] content = null;
        String query = null;
        String result = null;

        LOG.info("Started");

        XmldbConnector connector = new ExistConnector(HOST, PORT);

        try {

            LOG.info("Storing resources");
            uri = "/db/data/foo/foo-001.xml";
            connector.storeResource(uri, new File(RESOURCES_DIR, uri));
            uri = "/db/data/foo/foo-002.xml";
            connector.storeResource(uri, new File(RESOURCES_DIR, uri));
            uri = "/db/data/bar/bar-001.xml";
            connector.storeResource(uri, new File(RESOURCES_DIR, uri));
            uri = "/db/data/bar/bar-002.xml";
            connector.storeResource(uri, new File(RESOURCES_DIR, uri));
            uri = "/db/modules/greeter1.xql";
            connector.storeResource(uri, new File(RESOURCES_DIR, uri));
            uri = "/db/modules/greeter2.xql";
            connector.storeResource(uri, new File(RESOURCES_DIR, uri));

            LOG.info("Listing the collection '/db/data':");
            col = connector.retrieveCollection("/db/data");
            printCollection(col);

            LOG.info("Listing the collection '/db/data/foo':");
            col = connector.retrieveCollection("/db/data/foo");
            printCollection(col);

            LOG.info("Retrieving the raw content of a resource");
            uri = "/db/data/foo/foo-001.xml";
            content = connector.retrieveResource(uri);
            LOG.info("Resource content:\n" + new String(content));

            LOG.info("Retrieving the content of an XML document");
            uri = "/db/data/foo/foo-001.xml";
            doc = connector.retrieveXmlDocument(uri);
            if (doc != null) {
                LOG.info("XML document content:\n" + doc.asXML());
            } else {
                LOG.error("Document not found");
            }

            LOG.info("Executing an ad-hoc query");
            // query = "for $x in (1, 2, 3) return <Number>{$x}</Number>";
            query = "for $doc in collection('/db/data/foo') return <id>{$doc/document/id/text()}</id>";
            result = connector.executeQuery(query);
            LOG.info("Result:\n" + result);

            LOG.info("Calling an executable XQuery module");
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("name", "Mr. Jackson");
            result = connector.callModule("/db/modules/greeter1.xql", paramMap);
            LOG.info("Result:\n" + result);

            LOG.info("Calling an XQuery library function");
            String[] params = new String[] { "Mr. Smith" };
            result = connector.callFunction("http://www.example.org/greeter", "/db/modules/greeter2.xql", "greeting", params);
            LOG.info("Result:\n" + result);

            LOG.info("Deleting resources");
            connector.deleteResource("/db/data");
            connector.deleteResource("/db/modules");

        } catch (XmldbException e) {
            LOG.error("Database error: " + e.getMessage(), e);
        }

        LOG.info("Finished");
    }
    
    private static void printCollection(Collection col) {
        for (Resource resource : col.getResources()) {
            if (resource instanceof Collection) {
                LOG.info(String.format("  [%s]", resource.getName()));
            } else {
                LOG.info(String.format("  %s", resource.getName()));
            }
        }
    }

}
