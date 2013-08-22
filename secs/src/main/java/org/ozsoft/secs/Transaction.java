package org.ozsoft.secs;

/**
 * SECS data message transaction consisting of a pair of a primary message and corresponding reply message.
 * 
 * @author Oscar Stigter
 */
public class Transaction {

    /** The timestamp of the start of the transaction. */
    private final long timestamp;

    /** The primary message. */
    private final Message primaryMessage;

    /** The reply message. */
    private Message replyMessage;

    /**
     * Constructor.
     * 
     * @param primaryMessage
     *            The primary message.
     */
    public Transaction(Message primaryMessage) {
        timestamp = System.currentTimeMillis();
        this.primaryMessage = primaryMessage;
    }

    /**
     * Returns the timestamp of the start of the transaction.
     * 
     * @return The timestamp of the start of the transaction.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Returns the primary message.
     * 
     * @return The primary message.
     */
    public Message getPrimaryMessage() {
        return primaryMessage;
    }

    /**
     * Returns the reply message.
     * 
     * @return The reply message.
     */
    public Message getReplyMessage() {
        return replyMessage;
    }

    /**
     * Sets the reply message.
     * 
     * @param replyMessage
     *            The reply message.
     */
    /* package */void setReplyMessage(Message replyMessage) {
        if (replyMessage.getTransactionId() != primaryMessage.getTransactionId()) {
            throw new IllegalArgumentException("Reply message does not match request message");
        }
        this.replyMessage = replyMessage;
    }

}
