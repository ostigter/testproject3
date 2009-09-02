package org.ozsoft.webdav;

/**
 * HTTP/WebDAV status.
 * 
 * @author Oscar Stigter
 */
public enum WebDavStatus {
    
    /** OK. */
    OK(200, "OK"),
    
    /** Invalid request. */
    INVALID_REQUEST(401, "Invalid Request"),
    
    /** Resource not found. */
    NOT_FOUND(404, "Not Found"),
    
    ;
    
    /** The code. */
    private final int code;
    
    /** The description. */
    private final String description;
    
    /**
     * Constructor.
     * 
     * @param code
     *            The code.
     * @param description
     *            The description.
     */
    WebDavStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * Returns the code.
     * 
     * @return The code.
     */
    public int getCode() {
        return code;
    }
    
    /**
     * Returns the description.
     * 
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

}
