package org.ozsoft.secs;

/**
 * SECS configuration related exception.
 * 
 * @author Oscar Stigter
 */
public class SecsConfigurationException extends SecsException {

    private static final long serialVersionUID = -4933626210642528421L;

    /**
     * Constructor with a message only.
     * 
     * @param message
     *            Message describing the problem.
     */
    public SecsConfigurationException(String message) {
        super(message);
    }

    /**
     * Constructor with a message and an inner exception as cause.
     * 
     * @param message
     *            Message describing the problem.
     * @param cause
     *            Inner exception with the actual cause of the problem.
     */
    public SecsConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
