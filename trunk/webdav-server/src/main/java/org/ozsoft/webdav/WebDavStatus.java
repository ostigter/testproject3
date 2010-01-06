package org.ozsoft.webdav;

/**
 * HTTP/WebDAV status.
 * 
 * @author Oscar Stigter
 */
public enum WebDavStatus {
    
    /** OK. */
    OK(200, "OK"),
    
    /** OK. */
    CREATED(201, "Created"),
    
    /** WebDAV multistatus. */
    MULTI_STATUS(207, "MultiStatus"),
    
    /** Invalid request. */
    INVALID_REQUEST(401, "Invalid Request"),
    
    /** Resource not found. */
    NOT_FOUND(404, "Not Found"),
    
    ;
    
    /** The code. */
    private final int code;
    
    /** The name. */
    private final String name;
    
    /**
     * Constructor.
     * 
     * @param code
     *            The code.
     * @param name
     *            The name.
     */
    WebDavStatus(int code, String name) {
        this.code = code;
        this.name = name;
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
     * Returns the name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

}
