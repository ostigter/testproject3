package xen;


import java.io.File;
import java.util.HashSet;
import java.util.Set;


/**
 * Collection.
 * 
 * @author Oscar Stigter
 */
public class Collection {
	
	
	/** Back-reference to the database. */
	private DatabaseImpl database;

	/** ID. */
	private int id;
	
	/** Name. */
	private String name;
	
	/** Parent collection. */
	private Collection parent;
	
	/** Child collections. */
	private Set<Collection> collections;
	
	/** Documents. */
	private Set<Document> documents;
	
	
    //------------------------------------------------------------------------
    //  Constructors
    //------------------------------------------------------------------------

	
	/* package */ Collection(
			DatabaseImpl database, String name, Collection parent) {
		this.database = database;
		this.name = name;
		this.parent = parent;
		
		id = database.getNextId(); 
		
		collections = new HashSet<Collection>();
		documents = new HashSet<Document>();
		
		// Create corresponding directory on file system.
		File dir = new File(database.getDatabaseDir() + getUri());
		if (!dir.exists()) {
		    dir.mkdir();
		}
	}
	
	
    //------------------------------------------------------------------------
    //  Accessor methods
    //------------------------------------------------------------------------

	
	public String getName() {
		return name;
	}
	
	
	public Collection getParent() {
		return parent;
	}
	
	
	public Set<Collection> getCollections() {
		return collections;
	}
	

	public Set<Document> getDocuments() {
		return documents;
	}
	
	
    //------------------------------------------------------------------------
    //  Non-accessor methods
    //------------------------------------------------------------------------
    
	
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
		Collection col = new Collection(database, name, this);
		collections.add(col);
		return col;
	}
	
	
	public Document createDocument(String name) {
		Document doc = new Document(database, name, this);
		documents.add(doc);
		return doc;
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
