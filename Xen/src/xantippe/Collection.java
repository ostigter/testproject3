package xantippe;


import java.io.File;
import java.util.Set;
import java.util.TreeSet;


/**
 * Collection.
 * 
 * @author Oscar Stigter
 */
public class Collection implements Comparable<Collection> {
	
	
	/** Back-reference to the database. */
	private DatabaseImpl database;

	/** ID. */
	private int id;
	
	/** Name. */
	private String name;
	
	/** Parent collection. */
	private int parent;
	
	/** Child collections. */
	private Set<Integer> collections;
	
	/** Documents. */
	private Set<Integer> documents;
	
	/** Indices. */
	private Set<Index> indices;
	
	
    //------------------------------------------------------------------------
    //  Constructors
    //------------------------------------------------------------------------

	
	/* package */ Collection(
			DatabaseImpl database, int id, String name, int parent) {
	    this.id = id;
		this.database = database;
		this.name = name;
		this.parent = parent;
		
		collections = new TreeSet<Integer>();
		documents = new TreeSet<Integer>();
		indices = new TreeSet<Index>();
		
		// Create corresponding directory on file system.
		File dir = new File(database.getDatabaseDir() + getUri());
		if (!dir.exists()) {
		    dir.mkdir();
		}
		
		database.addCollection(this);
	}
	
	
    //------------------------------------------------------------------------
    //  Public methods
    //------------------------------------------------------------------------

	
	public String getName() {
		return name;
	}
	
	
	public Collection getParent() {
		return database.getCollection(parent);
	}
	
	
	public Set<Collection> getCollections() {
	    Set<Collection> cols = new TreeSet<Collection>();
	    
	    for (int id : collections) {
	        cols.add(database.getCollection(id));
	    }
	    
		return cols;
	}
	

	public Set<Document> getDocuments() {
        Set<Document> docs = new TreeSet<Document>();
        
        for (int id : documents) {
            docs.add(database.getDocument(id));
        }
        
        return docs;
	}
	
	
	public Set<Index> getIndices() {
		Set<Index> indices2 = new TreeSet<Index>();
		
		// Add indices from this collection.
		indices2.addAll(indices);
		
		// Add inherited indices from parent collections.
		Collection col = getParent();
		if (col != null) {
			indices2.addAll(col.getIndices());
		}
		
		return indices2;
	}
	
	
	public Index getIndex(String name) {
		Index index = null;
		
		Set<Index> inheritedIndices = getIndices();
		for (Index i : inheritedIndices) {
			if (i.getName().equals(name)) {
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	
	public void addIndex(String name, String path) {
		Index index = getIndex(name);
		if (index == null) {
			int newId = database.getNextId();
			indices.add(new Index(newId, name, path));
		} else {
			String msg = "Index name already used (possibly inherited)";
			throw new IllegalArgumentException(msg);
		}
	}
	
	
	public String getUri() {
		StringBuilder sb = new StringBuilder();
		
		Collection col = this;
		while (col != null) {
			sb.insert(0, '/'); 
			sb.insert(1, col.getName());
			col = col.getParent();
		}
		
		return sb.toString();
	}
	
	
	public Collection createCollection(String name) {
	    int childId = database.getNextId(); 
		Collection col = new Collection(database, childId, name, id);
		collections.add(childId);
		return col;
	}
	
	
	public Document createDocument(String name) {
		Document doc = new Document(database, name, this);
		documents.add(doc.getId());
		return doc;
	}
	
	
    //------------------------------------------------------------------------
    //  Interface implementation: Comparable
    //------------------------------------------------------------------------

	
	public int compareTo(Collection col) {
		return name.compareTo(col.getName());
	}
	

    //------------------------------------------------------------------------
    //  Overriden methods: Object
    //------------------------------------------------------------------------

	
	@Override
	public String toString() {
		return getUri();
	}
	

    //------------------------------------------------------------------------
    //  Package protected methods
    //------------------------------------------------------------------------

	
	/* package */ int getId() {
		return id;
	}


}
