package org.ozsoft.xmlindexer;

/**
 * Index for an XML element.
 */
public class Index {

    /** Some name identifying this index. */
    private final String name;

    /** The path of the indexed element. */
    private final String path;

    /** The indexed value (if found). */
    private String value;

    /**
     * Constructs an index.
     * 
     * @param name
     *            The name.
     * @param path
     *            The path.
     */
    public Index(String name, String path) {
        this.name = name;
        this.path = path;
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
     * Returns the path.
     * 
     * @return The path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns the value. <br />
     * 
     * A <code>null</code> means no value has been found.
     * 
     * @return The value, or null if not found.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     * 
     * @param value The value.
     */
    /* package */ void setValue(String value) {
        this.value = value;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s ('%s' = '%s')", name, path, value);
    }

}
