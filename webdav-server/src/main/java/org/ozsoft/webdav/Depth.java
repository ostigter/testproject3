package org.ozsoft.webdav;

/**
 * WebDAV Depth header values.
 * 
 * @author Oscar Stigter
 */
public enum Depth {
	
	/** Depth of 0 (top level resource only). */
	ZERO("0"),
	
	/** Depth of 1 (resource and its direct children). */
	ONE("1"),
	
	/** Infinite depth 1 (resource and all of its descendants). */
	INFINITY("infinity"),
	
	;
	
	/** The name. */
	private final String name;
	
	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The name.
	 */
	Depth(String name) {
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
	
	/**
	 * Parses a string as a depth value.
	 * 
	 * @param name
	 *            The string to parse as depth.
	 * 
	 * @return The depth if parsed successfully, otherwise null.
	 */
	public static Depth parse(String name) {
		for (Depth depth : values()) {
			if (depth.getName().equals(name)) {
				return depth;
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

}
