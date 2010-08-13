package org.ozsoft.xmldb;

/**
 * XMLDB exception indicating that the user is not authorized to perform a
 * specific operation on the database.
 * 
 * @author Oscar Stigter
 */
public class NotAuthorizedException extends XmldbException {
    
    /** Serial version UID. */
    private static final long serialVersionUID = -4506483007291569676L;

    /**
     * Constructor.
     * 
     * @param uri
     *            The resource URI.
     */
    public NotAuthorizedException(String message) {
        super(message);
    }

}
