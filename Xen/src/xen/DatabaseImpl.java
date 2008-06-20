package xen;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Default implementation of the Document Manager.
 * 
 * @author  Oscar Stigter
 */
public class DatabaseImpl implements Database {
	
	
    /** Database directory. */
    private static final String DB_DIR = "data";
    
    /** Whether the database is running. */
    private boolean isRunning;

	/** Root collection. */
	private Collection rootCollection;
	
	/** Documents mapped by ID. */
	private Map<Integer, Document> documents;
	
    /** Next document ID. */
    private int nextId;
    
    /** Indexes mapped by key name. */
    private Map<String, Index> indexes;
    
	
    //------------------------------------------------------------------------
    //  Constructors
    //------------------------------------------------------------------------


    /**
     * Zero-argument constructor.
     */
    public DatabaseImpl() {
		documents = new HashMap<Integer, Document>();
        indexes = new HashMap<String, Index>();
		
		isRunning = false;
	}
    
    
    //------------------------------------------------------------------------
    //  Interface implementation: Database
    //------------------------------------------------------------------------
    

    public void start() throws XmldbException {
        if (isRunning) {
            throw new XmldbException("Database already running");
        }
        
        // Create database directory if necessary. 
        File dir = new File(DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        // TODO: Use persistent storage for collections.
        if (rootCollection == null) {
            rootCollection = new Collection(this, "db", null);
        }
        
        readMetaData();
        
        readCollections();
        
        isRunning = true;
    }
    
    
    public void shutdown() throws XmldbException {
        checkRunning();
        
        writeMetaData();
        
        writeCollections();
        
        isRunning = false;
    }
    
    
    public boolean isRunning() {
        return isRunning;
    }
    
    
    public Collection getRootCollection() throws XmldbException {
        checkRunning();
    	return rootCollection;
    }
    
    
	@SuppressWarnings("unchecked")  // new Set[]
    public Set<Document> findDocuments(Key[] keys) throws XmldbException {
	    checkRunning();
	    
        int noOfKeys = keys.length;
        Set<Integer>[] docsPerKey = new Set[noOfKeys];

        // Find documents (ID's) that match any key.
        for (int i = 0; i < noOfKeys; i++) {
            Key key = keys[i];
            docsPerKey[i] = new HashSet<Integer>();
            Index index = indexes.get(key.getName());
            if (index != null) {
                docsPerKey[i].addAll(index.findDocuments(key.getValue()));
            }
        }
        
        // Gather documents by ID's.
        Set<Document> docs = new HashSet<Document>();
        if (noOfKeys == 1) {
            for (int id : docsPerKey[0]) {
                docs.add(documents.get(id));
            }
        } else {
            // Filter out documents that do not match ALL keys.
            Set<Integer> matchingIds = new HashSet<Integer>();
            for (int id : docsPerKey[0]) {
                boolean matches = true;
                for (int i = 1; i < noOfKeys; i++) {
                    if (!docsPerKey[i].contains(id)) {
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    matchingIds.add(id);
                }
            }
            
            for (int id : matchingIds) {
                docs.add(documents.get(id));
            }
        }
	    
	    return docs;
	}
	
	
    //------------------------------------------------------------------------
    //  Package protected methods
    //------------------------------------------------------------------------
    
    
    /* package */ String getDatabaseDir() {
        return DB_DIR;
    }
    
    
	/* package */ int getNextId() {
		return nextId++;
	}
	
	
	/* package */ void addDocument(Document doc) {
		documents.put(doc.getId(), doc);
	}
	
	
    /* package */ void indexDocument(Document doc) {
//        System.out.println("Indexing document: " + doc);
        for (Key key : doc.getKeys()) {
            String keyName = key.getName();
            Index index = indexes.get(keyName);
            if (index == null) {
                index = new Index();
                indexes.put(keyName, index);
            }
            index.indexDocument(doc, key.getValue());
        }
    }
    
    
    //------------------------------------------------------------------------
    //  Private methods
    //------------------------------------------------------------------------
    
    
    private void checkRunning() throws XmldbException {
        if (!isRunning) {
            throw new XmldbException("Database not running");
        }
    }
    
    
    private void readMetaData() {
    	File file = new File(DB_DIR + "/metadata.dbx");
    	if (file.exists()) {
    		try {
    			DataInputStream dis =
    					new DataInputStream(new FileInputStream(file));
    			nextId = dis.readInt();
    			dis.close();
    		} catch (IOException e) {
    			System.err.println("ERROR: Could not read metadata.dbx: " + e);
    		}
    	} else {
    		nextId = 1;
    	}
    }
    
    
    private void writeMetaData() {
    	File file = new File(DB_DIR + "/metadata.dbx");
		try {
			DataOutputStream dos =
					new DataOutputStream(new FileOutputStream(file));
			dos.writeInt(nextId);
			dos.close();
		} catch (IOException e) {
			System.err.println(e);
		}
    }
    
    
    private void readCollections() {
    	File file = new File(DB_DIR + "/collections.dbx");
    	if (file.exists()) {
    		try {
    			DataInputStream dis =
    					new DataInputStream(new FileInputStream(file));
    			dis.close();
    		} catch (IOException e) {
    			System.err.println("ERROR: Could not read collections.dbx: " + e);
    		}
    	} else {
    		nextId = 1;
    	}
    }
    
    
    private void writeCollections() {
    	File file = new File(DB_DIR + "/collections.dbx");
		try {
			DataOutputStream dos =
					new DataOutputStream(new FileOutputStream(file));
			writeCollection(rootCollection, dos);
			dos.close();
		} catch (IOException e) {
			System.err.println("ERROR: Could not write collections.dbx: " + e);
		}
    }
    
    
    private void writeCollection(Collection col, DataOutputStream dos)
    		throws IOException {
    	dos.writeInt(col.getId());
    	dos.writeUTF(col.getName());
    	Set<Document> docs = col.getDocuments();
    	dos.writeInt(docs.size());
    	for (Document doc : docs) {
    		dos.writeInt(doc.getId());
    	}
    	Set<Collection> cols = col.getCollections();
    	dos.writeInt(cols.size());
    	for (Collection c : cols) {
    		writeCollection(c, dos);
    	}
    }
    
    
    
    
    
//    private void flush(Collection col) {
//        System.out.println("Flushing collection: " + col);
//        
//        File dir = new File(DB_DIR + col.getUri());
//        if (!dir.exists()) {
//        	dir.mkdirs();
//        }
//        
//    	for (Document doc : col.getDocuments()) {
//    		flush(doc);
//    	}
//    	
//    	for (Collection col2 : col.getCollections()) {
//    		flush(col2);
//    	}
//    }
//    
//    
//    private void flush(Document doc) {
//		if (doc.isDirty()) {
//	        System.out.println("Flushing document: " + doc);
//	        
//			try {
//				OutputStream os = new FileOutputStream(DB_DIR + doc.getUri());
//				os.write(doc.getContent().getBytes());
//				os.close();
//			} catch (IOException e) {
//				System.err.println("Could not store document: " + e.getMessage());
//			}
//			
//			indexDocument(doc);
//			
//			doc.setDirty(false);
//		}
//    }
	

}
