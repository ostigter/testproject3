package org.ozsoft.courier;

/**
 * A Courier specific exception.
 *  
 * @author Oscar Stigter
 */
public class CourierException extends Exception {
    
    /** The serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor with a message.
     * 
     * @param msg
     *            The message.
     */
    public CourierException(String msg) {
        super(msg);
    }
    
    /**
     * Constructor with a message and an inner exception.
     * 
     * @param msg
     *            The message.
     * @param t
     *            The inner exception.
     */
    public CourierException(String msg, Throwable t) {
        super(msg, t);
    }

}
