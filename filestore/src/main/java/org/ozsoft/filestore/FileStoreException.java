package org.ozsoft.filestore;

/**
 * FileStore exception.
 * 
 * @author Oscar Stigter
 */
public class FileStoreException extends Exception {
    
    /** Serial version UID. */
    private static final long serialVersionUID = -3236866746926433857L;
    
    /**
     * Constructor.
     * 
     * @param message
     *            The message describing the cause.
     */
    public FileStoreException(String message) {
        super(message);
    }

    /**
     * Constructor with a nested exception.
     * 
     * @param message
     *            The message describing the cause.
     * @param cause
     *            The causing exception.
     */
    public FileStoreException(String message, Throwable cause) {
        super(message, cause);
    }

}
