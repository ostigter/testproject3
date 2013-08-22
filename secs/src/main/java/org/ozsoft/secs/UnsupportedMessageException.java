package org.ozsoft.secs;

/**
 * Indicates an unsupported SECS message.
 * 
 * @author Oscar Stigter
 */
public class UnsupportedMessageException extends SecsException {

    private static final long serialVersionUID = -1434846673624462650L;
    
    private final int stream;
    
    private final int function;
    
    private final long transactionId;

    /**
     * Constructor.
     * 
     * @param stream
     *            The message's stream.
     * @param function
     *            The message's function.
     * @param transactionId
     *            The message's transaction ID.
     */
    public UnsupportedMessageException(int stream, int function, long transactionId) {
        super(String.format("Unsupported SECS message: S%dF%d", stream, function));
        this.stream = stream;
        this.function = function;
        this.transactionId = transactionId;
    }
    
    /**
     * Returns the stream of the message.
     * 
     * @return The stream.
     */
    public int getStream() {
        return stream;
    }
    
    /**
     * Returns the function of the message.
     * 
     * @return The function.
     */
    public int getFunction() {
        return function;
    }

    /**
     * Returns the transaction ID of the message.
     * 
     * @return The transaction ID.
     */
    public long getTransactionId() {
        return transactionId;
    }

}
