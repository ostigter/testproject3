package ozmud.world;


import java.io.Serializable;


/**
 * Abstract base class of all MUD objects.
 * 
 * @author Oscar Stigter
 *
 */
public abstract class MudObject implements Serializable {


	/** Serial version UID. */
	private static final long serialVersionUID = 1L;
	
	/** Short name. */
	private String shortName;
	
	/** Full name. */
	private String fullName;
	
	/** Description. */
	private String description;
	
	/** Aliasses. */
	private String[] aliasses;
	
	
	/**
	 * Default constructor.
	 */
	public MudObject() {
		// Empty implementation.
	}


	/**
	 * Returns the short name.
	 * 
	 * @return  The short name
	 */
	public String getShortName() {
		return shortName;
	}


	/**
	 * Sets the short name.
	 * 
	 * @param shortName  The short name
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	
	/**
	 * Returns the full name.
	 * 
	 * @return  The full name
	 */
	public String getFullName() {
		return fullName;
	}


	/**
	 * Sets the full name.
	 * 
	 * @param fullName  The full name
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	
	/**
	 * Returns the description.
	 * 
	 * @return  The description
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * Sets the description.
	 * 
	 * @param description  The description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	/**
	 * Sets the aliasses.
	 * 
	 * @param aliasses  The aliasses
	 */
	public void setAliasses(String[] aliasses) {
		this.aliasses = aliasses;
	}


	/**
	 * Returns true if this object matches the specified name.
	 * The name is compared to the object's shortname and aliasses.
	 * 
	 * @param name  The name
	 * 
	 * @return True if a match, otherwise false
	 */
	public boolean matches(String name) {
		if (name != null && name.equalsIgnoreCase(shortName)) {
			return true;
		} else {
			if (aliasses != null) {
				for (String alias : aliasses) {
					if (alias.equalsIgnoreCase(name)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Returns the <code>String</code> representation of this object.
	 * 
	 * @return The <code>String</code> representation of this object
	 */
	@Override
	public String toString() {
		return fullName;
	}


}
