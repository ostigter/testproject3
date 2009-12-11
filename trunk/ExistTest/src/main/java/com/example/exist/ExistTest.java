package com.example.exist;

import org.apache.log4j.Logger;
import org.exist.xmldb.XQueryService;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 * Test application for the eXist XML:DB API.
 * 
 * @author Oscar Stigter
 */
public class ExistTest {

    private static final String BASE_URI = "xmldb:exist://localhost:8080/exist/xmlrpc";

    private static final String USERNAME = "admin";

    private static final String PASSWORD = "admin";

    private static final String DRIVER = "org.exist.xmldb.DatabaseImpl";

    private static Logger LOGGER = Logger.getLogger(ExistTest.class);

    public ExistTest() {
	run();
    }

    public static void main(String[] args) {
	new ExistTest();
    }

    private void run() {
	try {
	    initializeDatabase();

	    // Browse a collection.
	    Collection col = getCollection("/db/data/foo");
	    LOGGER.debug("Numer of resources: " + col.getResourceCount());
	    for (String name : col.listResources()) {
		Resource res = col.getResource(name);
		LOGGER.info("Resource name: " + res.getId());
		String content = (String) res.getContent();
		LOGGER.debug("Resource content:\n" + content);
	    }

	    // Execute a stand-alone query.
	    LOGGER.info("Executing query 1");
	    String query = "concat('Hello', ' World!')";
	    LOGGER.info("Result:\n" + executeQuery(query));

	    // Execute a query on documents in the database.
	    LOGGER.info("Executing query 2");
	    query = "doc('/db/data/foo/foo-001.xml')/document/id/text()";
	    LOGGER.info("Result:\n" + executeQuery(query));

	} catch (XMLDBException e) {
	    LOGGER.error("Database error", e);
	}
    }

    private static void initializeDatabase() throws XMLDBException {
	try {
	    // Register database driver.
	    Class<?> cl = Class.forName(DRIVER);
	    Database database = (Database) cl.newInstance();
	    DatabaseManager.registerDatabase(database);
	} catch (Exception e) {
	    throw new XMLDBException(ErrorCodes.INVALID_DATABASE,
		    "Could not instantiate database");
	}
    }

    private static Collection getCollection(String uri) throws XMLDBException {
	Collection col = DatabaseManager.getCollection(BASE_URI + uri, USERNAME, PASSWORD);
	if (col == null) {
	    throw new XMLDBException(ErrorCodes.NO_SUCH_COLLECTION,
		    String.format("Collection not found: '%s'", uri));
	}
	return col;
    }

    private static String executeQuery(String query) throws XMLDBException {
	Collection rootCol = getCollection("/db");
	XQueryService xqueryService =
	    	(XQueryService) rootCol.getService("XQueryService", "1.0");
	CompiledExpression expression = xqueryService.compile(query);
	StringBuilder sb = new StringBuilder();
	ResourceSet rs = xqueryService.execute(expression);
	ResourceIterator it = rs.getIterator();
	while (it.hasMoreResources()) {
	    Resource res = it.nextResource();
	    if (res instanceof XMLResource) {
		sb.append(res.getContent());
	    }
	}
	return sb.toString();
    }

}
