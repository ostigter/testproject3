package xen;


import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;


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
		
    		// Create a collection for ADELcd documents.
//		    Collection rootCol = database.getRootCollection();
//    		Collection adelCdCol = rootCol.createCollection("ADELcd");
    		
//    		// Create an ADELcd document.
//    		Document doc = adelCdCol.createDocument("ADELcd_0001.xml");
//    		doc.setKey("DocumentType", "ADELcd");
//    		doc.setKey("DocumentId", "ADELcd_0001");
//            doc.setKey("LotId", "LotId_0001");
//    		doc.setContent("<Document>\n  <DocumentId>ADELcd_0001</DocumentId>\n</Document>");
    
            // Retrieve an ADELcd document.
            Key[] keys = new Key[] {
            		new Key("DocumentType", "ADELcd"),
                    new Key("LotId",        "LotId_0001"),
    		};
    		Set<Document> docs = database.findDocuments(keys);
            if (docs.size() == 0) {
                logger.info("No document found.");
            } else {
            	logger.info("Documents found:");
                for (Document d : docs) {
                	logger.info("  " + d);
                }
            }
            
            database.shutdown();
		} catch (XmldbException e) {
			logger.error(e);
		}
		
		logger.debug("DatabaseTest finished.");
	}


}
