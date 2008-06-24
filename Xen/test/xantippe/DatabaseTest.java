package xantippe;


import org.apache.log4j.Logger;

import xantippe.Collection;
import xantippe.Database;
import xantippe.DatabaseImpl;
import xantippe.Util;
import xantippe.XmldbException;


/**
 * Test driver for the database.
 * 
 * @author Oscar Stigter
 */
public class DatabaseTest {
	
	
	private static final Logger logger = Logger.getLogger(DatabaseTest.class);
	
	
	public static void main(String[] args) {
    	Util.initLog4j();
    	
		logger.debug("DatabaseTest started.");
		
		Database database = new DatabaseImpl();
		
		try {
		    database.start();
		
    		// Create collections.
		    Collection rootCol = database.getRootCollection();
		    Collection dataCol = rootCol.createCollection("data");
		    Collection fooCol = dataCol.createCollection("Foo");
		    Collection modulesCol = rootCol.createCollection("modules");
    		
		    // Create indices.
		    dataCol.addIndex("DocumentId", "/Document/Id");
		    fooCol.addIndex("DocumentType", "/Document/Type");
		    logger.info(dataCol.getIndices());
		    logger.info(fooCol.getIndices());
		    
//    		// Add documents.
//    		Document doc = fooCol.createDocument("0001.xml");
//    		doc.setKey("DocumentId", 1);
//    		doc.setKey("DocumentType", "Foo");
//    		doc.setContent("<Document>\n  <Id>1</Id>\n  <Type>Foo</Type>\n</Document>");
//    		doc = fooCol.createDocument("0002.xml");
//    		doc.setKey("DocumentId", 2);
//    		doc.setKey("DocumentType", "Foo");
//    		doc.setContent("<Document>\n  <Id>2</Id>\n  <Type>Foo</Type>\n</Document>");
//    		doc = fooCol.createDocument("0003.xml");
//    		doc.setKey("DocumentId", 3);
//    		doc.setKey("DocumentType", "Bar");
//    		doc.setContent("<Document>\n  <Id>3</Id>\n  <Type>Bar</Type>\n</Document>");
//    
//            // Retrieve a document.
//            Key[] keys = new Key[] {
//            		new Key("DocumentType", "Foo"),
////                    new Key("DocumentId",   2),
//    		};
//    		Set<Document> docs = database.findDocuments(keys);
//            if (docs.size() == 0) {
//                logger.info("No document found.");
//            } else {
//            	logger.info("Documents found:");
//                for (Document d : docs) {
//                	logger.info("  " + d);
//                }
//            }
            
            database.shutdown();
            
		} catch (XmldbException e) {
			logger.error(e);
		}
		
		logger.debug("DatabaseTest finished.");
	}


}
