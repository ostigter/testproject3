package org.ozsoft.webdav;

/**
 * A WebDAV propstat element.
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
    
    public String getName() {
        return name;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    public WebDavStatus getStatus() {
        return status;
    }
    
    public void setStatus(WebDavStatus status) {
        this.status = status;
    }

}
