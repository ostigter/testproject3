package xen;


import java.util.Set;


/**
 * Test driver for the database.
 * 
 * @author Oscar Stigter
 */
public class DatabaseTest {
	
	
	public static void main(String[] args) {
		Database database = new DatabaseImpl();
		
		try {
		    database.start();
		
    		// Create a collection for ADELcd documents.
		    Collection rootCol = database.getRootCollection();
    		Collection adelCdCol = rootCol.createCollection("ADELcd");
    		
    		// Create an ADELcd document.
    		Document doc = adelCdCol.createDocument("ADELcd_0001.xml");
    		doc.setKey("DocumentType", "ADELcd");
    		doc.setKey("DocumentId", "ADELcd_0001");
            doc.setKey("LotId", "LotId_0001");
    		doc.setContent("<Document>\n  <DocumentId>ADELcd_0001</DocumentId>\n</Document>");
    
            // Retrieve an ADELcd document.
            Key[] keys = new Key[] {
            		new Key("DocumentType", "ADELcd"),
                    new Key("LotId",        "LotId_0001"),
    		};
    		Set<Document> docs = database.findDocuments(keys);
            if (docs.size() == 0) {
                System.out.println("No document found.");
            } else {
                System.out.println("Documents found:");
                for (Document d : docs) {
                    System.out.println("  " + d);
                }
            }
            
            database.stop();
		} catch (XmldbException e) {
		    System.err.println(e);
		}
	}


}
