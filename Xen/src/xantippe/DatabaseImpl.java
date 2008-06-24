package xantippe;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


/**
 * Default implementation of the database.
 * 
 * @author  Oscar Stigter
 */
public class DatabaseImpl implements Database {
	
	
	/** log4j logger. */
	private static final Logger logger = Logger.getLogger(DatabaseImpl.class);
	
    /** Database directory. */
    private static final String DB_DIR = "data";
    
    /** Meta data file. */
    private static final File metaDataFile =
    		new File(DB_DIR + "/metadata.dbx");
    
    /** Collections file. */
    private static final File collectionsFile =
    		new File(DB_DIR + "/collections.dbx");
    
    /** Whether the database is running. */
    private boolean isRunning = false;

	/** Root collection. */
	private Collection rootCollection;
	
	/** Collections mapped by ID. */
	private Map<Integer, Collection> collections;
    
	/** Documents mapped by ID. */
	private Map<Integer, Document> documents;
	
    /** Indexes mapped by key name. */
    private Map<String, IndexValue> indexes;
    
    /** Next document ID. */
    private int nextId;
    
	
    //------------------------------------------------------------------------
    //  Constructors
    //------------------------------------------------------------------------


    /**
     * Zero-argument constructor.
     */
    public DatabaseImpl() {
    	Util.initLog4j();
    	
    	collections = new HashMap<Integer, Collection>();
    	documents = new HashMap<Integer, Document>();
        indexes = new HashMap<String, IndexValue>();
		
		logger.debug("Database created.");
	}
    
    
    //------------------------------------------------------------------------
    //  Interface implementation: Database
    //------------------------------------------------------------------------
    

    public void start() throws XmldbException {
        if (isRunning) {
            throw new XmldbException("Database already running");
        }
        
		logger.debug("Starting database.");
		
        // Create database directory if necessary. 
        File dir = new File(DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        readMetaData();
        
        readCollections();
        
        isRunning = true;

		logger.debug("Database started.");
    }
    
    
    public void shutdown() throws XmldbException {
        checkRunning();
        
		logger.debug("Shutting down database.");
		
        writeMetaData();
        
        writeCollections();
        
        isRunning = false;
        
		logger.debug("Database shut down.");
    }
    
    
    public boolean isRunning() {
        return isRunning;
    }
    
    
    public Collection getRootCollection() throws XmldbException {
        checkRunning();
    	return rootCollection;
    }
    
    
	@SuppressWarnings("unchecked")  // new HashSet[]
    public Set<Document> findDocuments(Key[] keys) throws XmldbException {
	    checkRunning();
	    
	    if (keys == null) {
	    	String msg = "findDocuments(): Null value for keys";
	    	logger.error(msg);
	    	throw new IllegalArgumentException(msg);
	    }
	    
        int noOfKeys = keys.length;
	    if (noOfKeys == 0) {
	    	String msg = "findDocuments(): Empty key array";
	    	logger.error(msg);
	    	throw new IllegalArgumentException(msg);
	    }
        
        Set<Integer>[] docsPerKey = new HashSet[noOfKeys];

        // Find documents (ID's) that match any key.
        for (int i = 0; i < noOfKeys; i++) {
            Key key = keys[i];
            docsPerKey[i] = new HashSet<Integer>();
            IndexValue index = indexes.get(key.getName());
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
	
	
	/* package */ Collection getCollection(int id) {
	    return collections.get(id);
	}
	
	
    /* package */ Document getDocument(int id) {
        return documents.get(id);
    }
    
    
    /* package */ void addCollection(Collection col) {
        collections.put(col.getId(), col);
    }
    
    
	/* package */ void addDocument(Document doc) {
		documents.put(doc.getId(), doc);
	}
	
	
    /* package */ void indexDocument(Document doc) {
        for (Key key : doc.getKeys()) {
            String keyName = key.getName();
            IndexValue index = indexes.get(keyName);
            if (index == null) {
                index = new IndexValue();
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
    	if (metaDataFile.exists()) {
    		try {
    			DataInputStream dis =
    					new DataInputStream(new FileInputStream(metaDataFile));
    			nextId = dis.readInt();
    			dis.close();
    		} catch (IOException e) {
    			System.err.println("ERROR: Could not read metadata file: " + e);
    		}
    	} else {
    		nextId = 1;
    	}
    }
    
    
    private void writeMetaData() {
		try {
			DataOutputStream dos =
					new DataOutputStream(new FileOutputStream(metaDataFile));
			dos.writeInt(nextId);
			dos.close();
		} catch (IOException e) {
			System.err.println(e);
		}
    }
    
    
    private void readCollections() {
    	if (collectionsFile.exists()) {
    		try {
    			DataInputStream dis = new DataInputStream(
    					new FileInputStream(collectionsFile));
    			rootCollection = readCollection(dis);
    			dis.close();
    		} catch (IOException e) {
    			System.err.println("ERROR: Could not read collections file: " + e);
    		}
    	} else {
    	    int id = getNextId();
            rootCollection = new Collection(this, id, "db", -1);
    	}
    }
    
    
    private Collection readCollection(DataInputStream dis) throws IOException {
        int id = dis.readInt();
        String name = dis.readUTF();
        int parent = dis.readInt();
        return new Collection(this, id, name, parent);
    }
    
    
    private void writeCollections() {
		try {
			DataOutputStream dos = new DataOutputStream(
					new FileOutputStream(collectionsFile));
			writeCollection(rootCollection, dos);
			dos.close();
		} catch (IOException e) {
			System.err.println("ERROR: Could not write collections file: " + e);
		}
    }
    
    
    private void writeCollection(Collection col, DataOutputStream dos)
    		throws IOException {
    	dos.writeInt(col.getId());
    	dos.writeUTF(col.getName());
    	Collection parent = col.getParent();
    	if (parent != null) {
            dos.writeInt(parent.getId());
    	} else {
    	    dos.writeInt(-1);
    	}
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
    
    
}
