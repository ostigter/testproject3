package org.ozsoft.fondsbeheer.filestore;

/**
 * Exception thrown by the FileStore class.
 * 
 * @author Oscar Stigter
 */
public class FileStoreException extends Exception {
    
    /** Serial version UID. */
    private static final long serialVersionUID = -1L;
    
    /* package */ FileStoreException(String message) {
        super(message);
    }

    /* package */ FileStoreException(String message, Throwable t) {
        super(message, t);
    }

}
