package xantippe;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * Document instance.
 * 
 * @author Oscar Stigter
 */
public class Document implements Comparable<Document> {
	
	
	/** Back-reference to the database. */
	private DatabaseImpl database;

	/** ID. */
	private int id;
	
	/** Name. */
	private String name;
	
	/** Parent collection. */
	private Collection parent;
	
	/** Keys mapped by their name. */
	private Map<String, Key> keys;
	
	
    //------------------------------------------------------------------------
    //  Constructors
    //------------------------------------------------------------------------

	
	/* package */ Document(
	        DatabaseImpl database, String name, Collection parent) {
	    this.database = database;
		this.id = database.getNextId();
		this.name = name;
		this.parent = parent;
		
		keys = new HashMap<String, Key>();
		
		database.addDocument(this);
	}
	
	
    //------------------------------------------------------------------------
    //  Public methods
    //------------------------------------------------------------------------

	
	public String getName() {
		return name;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	public Collection getParent() {
		return parent;
	}
	
	
	public Key[] getKeys() {
		return keys.values().toArray(new Key[0]);
	}


	public Object getKey(String name) {
		Key key = keys.get(name);
		return (key != null) ? key.getValue() : null;
	}
	
	
	public void setKey(String name, Object value) {
		Key key = keys.get(name);
		if (key == null) {
			key = new Key(name, value);
            keys.put(name, key);
		} else {
			key.setValue(value);
		}
	}
	
	
	public String getUri() {
		return parent.getUri() + '/' + name; 
	}
	
	
	public String getContent() {
		return null;
	}
	
	
	public void setContent(String content) throws XmldbException {
	    try {
            OutputStream os =
                    new FileOutputStream(database.getDatabaseDir() + getUri());
            os.write(content.getBytes());
            os.close();
	    } catch (IOException e) {
	        String msg = "Could not store document: " + this;
	        throw new XmldbException(msg, e);
	    }
	    
		database.indexDocument(this);
	}
	
	
    //------------------------------------------------------------------------
    //  Interface implementation: Comparable
    //------------------------------------------------------------------------

	
	public int compareTo(Document doc) {
		return name.compareTo(doc.getName());
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
