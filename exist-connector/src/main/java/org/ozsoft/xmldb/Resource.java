package org.ozsoft.xmldb;

/**
 * Resource stored in an XML database.
 * 
 * @author Oscar Stigter
 */
public class Resource {

	/** The name. */
	private final String name;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name.
	 */
	public Resource(String name) {
		this.name = name;
	}

	/**
	 * Returns the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
