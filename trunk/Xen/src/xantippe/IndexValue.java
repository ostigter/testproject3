package xantippe;


import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Document index based on a key value.
 * 
 * @author Oscar Stigter
 */
class IndexValue {
	

	/** Document ID's by key values. */
	private Map<Object, Set<Integer>> documents;
	
	
    //------------------------------------------------------------------------
    //  Constructors
    //------------------------------------------------------------------------

	
	public IndexValue() {
		documents = new TreeMap<Object, Set<Integer>>();
	}
	
	
    //------------------------------------------------------------------------
    //  Public methods
    //------------------------------------------------------------------------
	
    
	public void indexDocument(Document doc, Object value) {
		Set<Integer> docs = documents.get(value);
		if (docs == null) {
			docs = new TreeSet<Integer>();
			documents.put(value, docs);
		}
		docs.add(doc.getId());
	}
	
	
	public Set<Integer> findDocuments(Object value) {
	    Set<Integer> docs = new TreeSet<Integer>();
	    
	    Set<Integer> ids = documents.get(value);
	    if (ids != null) {
            docs.addAll(ids);
	    }
	    
	    return docs;
	}
	
	
}
