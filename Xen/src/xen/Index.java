package xen;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * Document index based on a key value.
 * 
 * @author Oscar Stigter
 */
class Index {
	

	/** Document ID's by key values. */
	private HashMap<Object, Set<Integer>> documents;
	
	
    //------------------------------------------------------------------------
    //  Constructors
    //------------------------------------------------------------------------

	
	/* package */ Index() {
		documents = new HashMap<Object, Set<Integer>>();
	}
	
	
    //------------------------------------------------------------------------
    //  Non-accessor methods
    //------------------------------------------------------------------------
	
    
	public void indexDocument(Document doc, Object value) {
		Set<Integer> docs = documents.get(value);
		if (docs == null) {
			docs = new HashSet<Integer>();
			documents.put(value, docs);
		}
		docs.add(doc.getId());
	}
	
	
	public Set<Integer> findDocuments(Object value) {
	    Set<Integer> docs = new HashSet<Integer>();
	    
	    Set<Integer> ids = documents.get(value);
	    if (ids != null) {
            docs.addAll(ids);
	    }
	    
	    return docs;
	}
	
	
}
