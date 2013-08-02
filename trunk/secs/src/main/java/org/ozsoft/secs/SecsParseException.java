package org.ozsoft.secs;

public class SecsParseException extends SecsException {

    private static final long serialVersionUID = -364064596812127277L;

    /**
     * Constructor with a message only.
     * 
     * @param message
     *            Message describing the problem.
     */
    public SecsParseException(String message) {
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
    public SecsParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
