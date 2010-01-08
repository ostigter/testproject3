package org.ozsoft.webdav;

/**
 * A WebDAV property.
 * 
 * Wrapper for a 'propstat' element as part of a 'multistatus' element.
 * 
 * @author Oscar Stigter
 */
public class PropStat {
    
    /** The property name. */
    private final String name;
    
    /** The property value. */
    private String value;
    
    /** The property status. */
    private WebDavStatus status;
    
    /**
     * Constructor.
     * 
     * @param name
     *            The property name.
     */
    public PropStat(String name) {
        this.name = name;
    }
    
    /**
     * Returns the property name.
     * 
     * @return The property name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the property value.
     * 
     * @return The property value, or null if not set.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Sets the property value.
     * 
     * @param value
     *            The property value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the property status.
     * 
     * @return The property status, or null if not set.
     */
    public WebDavStatus getStatus() {
        return status;
    }
    
    /**
     * Sets the property status.
     * 
     * @param status
     *            The property status.
     */
    public void setStatus(WebDavStatus status) {
        this.status = status;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%s = '%s'", name, value);
    }

}
