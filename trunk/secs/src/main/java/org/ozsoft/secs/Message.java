package org.ozsoft.secs;

public interface Message {
    
    int getSessionId();
    
    long getTransactionId();
    
    byte[] toByteArray();
    
}
