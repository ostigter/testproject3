package org.ozsoft.secs;

public class SecsException extends Exception {

    private static final long serialVersionUID = -6343582345850766217L;
    
    public SecsException(String message) {
        super(message);
    }

    public SecsException(String message, Throwable cause) {
        super(message, cause);
    }

}
