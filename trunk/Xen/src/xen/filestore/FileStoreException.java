package xen.filestore;


/**
 * Exception thrown by the <code>FileStore</code> class.
 * 
 * @author Oscar Stigter
 */
public class FileStoreException extends Exception {
	

	private static final long serialVersionUID = -1L;
	

	public FileStoreException(String message) {
		super(message);
	}


	public FileStoreException(String message, Throwable t) {
		super(message, t);
	}


}
