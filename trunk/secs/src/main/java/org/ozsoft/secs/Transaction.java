package org.ozsoft.secs;

import org.ozsoft.secs.message.Message;

/**
 * SECS transaction belonging to a sent request message.
 * 
 * @author Oscar Stigter
 */
public class Transaction {
    
    private final Message requestMessage;
    
    private final long timestamp;
    
    public Transaction(Message requestMessage) {
        this.requestMessage = requestMessage;
        timestamp = System.currentTimeMillis();
    }
    
    public Message getRequestMessage() {
        return requestMessage;
    }
    
    public long getTimestamp() {
        return timestamp;
    }

}
