package org.ozsoft.xmldb;

/**
 * XMLDB exception indicating that a specific resource does not exist.
 * 
 * @author Oscar Stigter
 */
public class NotFoundException extends XmldbException {
    
    /** Serial version UID. */
    private static final long serialVersionUID = 9118174838396459623L;

    /**
     * Constructor.
     * 
     * @param uri
     *            The resource URI.
     */
    public NotFoundException(String uri) {
        super(String.format("Resource not found: '%s'", uri));
    }

}
