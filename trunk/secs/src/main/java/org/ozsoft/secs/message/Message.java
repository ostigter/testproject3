package org.ozsoft.secs.message;

public interface Message {
    
    int getSessionId();
    
    long getTransactionId();
    
    byte[] toByteArray();
    
}
