package encryptor;


/**
 * Exception thrown by the Encryptor class.
 *
 * @author  Oscar Stigter
 */
public class EncryptionException extends Exception {
	
	
	/** Serial version UID. */
	private static final long serialVersionUID = -1L;
	
	
	/**
	 * Constructs an <code>EncryptionException</code> with a message.
	 * 
	 * @param  message  the message
	 */
	public EncryptionException(String message) {
		super(message);
	}


	/**
	 * Constructs an <code>EncryptionException</code> with a message and a
	 * nested <code>Throwable</code>.
	 * 
	 * @param  message  the message
	 * @param  t        the nested <code>Throwable</code>
	 */
	public EncryptionException(String message, Throwable t) {
		super(message, t);
	}

}
