package xmldb;


import java.io.File;
import java.util.HashMap;


/**
 * The database.
 * 
 * @author  Oscar Stigter
 */
public class Database {
    
    
    //------------------------------------------------------------------------
    //  Private attributes
    //------------------------------------------------------------------------
    
	private static final Database instance = new Database();
	
	private static final String databaseDir = "db";
	
	private final XmlWriter xmlWriter = new XmlWriter(); 
	
    private final HashMap<Integer, Node> nodes;
    
    private final HashMap<Integer, Document> documents;
    
	private int nextId;
    

    //------------------------------------------------------------------------
    //  Constructor
    //------------------------------------------------------------------------
    
    /**
     * Private constructor.
     */
    private Database() {
    	nodes = new HashMap<Integer, Node>();
    	documents = new HashMap<Integer, Document>(); 
    	
    	nextId = 1;
    	
    	File dir = new File(databaseDir);
    	if (!dir.exists()) {
    		dir.mkdirs();
    	}
    }
    

    //------------------------------------------------------------------------
    //  Public methods
    //------------------------------------------------------------------------
    
    /**
     * Returns the singleton instance.
     * 
     * @return  the singelton instance
     */
    public static Database getInstance() {
        return instance;
    }


    /**
     * Returns the next ID.
     * 
     * @return  the next ID
     */
    public int getNextId() {
        return nextId++;
    }
    

    /**
     * Stores a document.
     * 
     * @param  doc  the document
     * @param  uri  the URI to store the document at
     * 
     * @throws  XmldbException  if the document could not be stored
     */
    public void storeDocument(Document doc, String uri)
    		throws XmldbException {
    	xmlWriter.write(doc);
//    	documents.put(doc.getId(), doc);
    }
    

    /**
     * Retrieves a document by its ID.
     * 
     * @param  id  the ID
     * 
     * @return  the document
     * 
     * @throws XmldbException  if the document does not exist
     */
    public Document retrieveDocument(int id) throws XmldbException {
    	Document doc = documents.get(id);
    	
    	if (doc == null) {
    		throw new XmldbException(
    				"Document with ID " + id + " not found");
    	}
    	
        return doc;
    }
    
    
    public int getNoOfDocuments() {
        return documents.size();
    }
    
    
    //------------------------------------------------------------------------
    //  Package protected methods
    //------------------------------------------------------------------------
    
    /* package */ void storeNode(Node node) {
        nodes.put(node.getId(), node);
    }


    /* package */ Node getNode(int id) {
        return nodes.get(id);
    }


}
