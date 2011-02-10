package org.ozsoft.encryption;

/**
 * Exception thrown by the Encryptor class.
 * 
 * @author Oscar Stigter
 */
public class EncryptionException extends Exception {

    /** Serial version UID. */
    private static final long serialVersionUID = -734585949573627174L;

    /**
     * Constructs an <code>EncryptionException</code> with a message.
     * 
     * @param message
     *            the message
     */
    public EncryptionException(String message) {
        super(message);
    }

    /**
     * Constructs an <code>EncryptionException</code> with a message and a
     * nested exception as the cause.
     * 
     * @param message
     *            The message.
     * @param cause
     *            The exception that caused this exception.
     */
    public EncryptionException(String message, Throwable t) {
        super(message, t);
    }

}
