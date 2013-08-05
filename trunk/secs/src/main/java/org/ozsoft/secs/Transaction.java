package org.ozsoft.secs;

import org.ozsoft.secs.message.Message;

/**
 * SECS transaction belonging to a sent request message.
 * 
 * @author Oscar Stigter
 */
public class Transaction {
    
    private final long timestamp;
    
    private final Message requestMessage;
    
    private Message replyMessage;
    
    public Transaction(Message requestMessage) {
        timestamp = System.currentTimeMillis();
        this.requestMessage = requestMessage;
    }
    
    public long getTimestamp() {
        return timestamp;
    }

    public Message getRequestMessage() {
        return requestMessage;
    }
    
    public Message getReplyMessage() {
        return replyMessage;
    }
    
    /* package */ void setReplyMessage(Message replyMessage) {
        if (replyMessage.getTransactionId() != requestMessage.getTransactionId()) {
            throw new IllegalArgumentException("Reply message does not match request message");
        }
        this.replyMessage = replyMessage;
    }
    
}
