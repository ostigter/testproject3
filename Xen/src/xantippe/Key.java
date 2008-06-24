package xantippe;


/**
 * Document key using to find documents using indexes.
 * 
 * Basically a simple name/value combination.
 * 
 * @author Oscar Stigter
 */
public class Key {
	
	
	/** Key name. */
	private String name;
	
	/** Key value. */
	private Object value;
	
	
    //------------------------------------------------------------------------
    //  Constructors
    //------------------------------------------------------------------------

	
	public Key(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	
    //------------------------------------------------------------------------
    //  Accessor methods
    //------------------------------------------------------------------------
    
	
	public String getName() {
		return name;
	}
	
	
	public Object getValue() {
		return value;
	}
	
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	
    //------------------------------------------------------------------------
    //  Override methods: Object
    //------------------------------------------------------------------------
    

	@Override
	public String toString() {
		return "{" + name + " = " + value + "}";
	}

	
}
