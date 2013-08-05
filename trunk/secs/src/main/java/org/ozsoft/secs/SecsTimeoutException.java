package org.ozsoft.secs;

/**
 * SECS timeout exception.
 * 
 * @author Oscar Stigter
 */
public class SecsTimeoutException extends SecsException {

    private static final long serialVersionUID = 8246754378510512955L;

    /**
     * Constructor.
     * 
     * @param message
     *            Message describing the timeout.
     */
    public SecsTimeoutException(String message) {
        super(message);
    }

}
