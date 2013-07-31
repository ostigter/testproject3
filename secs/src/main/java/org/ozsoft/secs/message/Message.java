package org.ozsoft.secs.message;

public interface Message {
    
    int LENGTH_LENGTH = 4;
    
    int HEADER_LENGTH = 10;
    
    byte[] HEADER_LENGTH_BYTES = new byte[] {0, 0, 0, HEADER_LENGTH};
    
    int MIN_LENGTH = LENGTH_LENGTH + HEADER_LENGTH;
    
    long MAX_LENGTH = 256 * 1024; // 256 kB
    
    int SESSION_ID_LENGTH = 2;
    
    int SYSTEM_BYTES_LENGTH = 4;
    
    int POS_SESSIONID = 0;
    
    int POS_HEADERBYTE2 = 2;
    
    int POS_HEADERBYTE3 = 3;
    
    int POS_PTYPE = 4;
    
    int POS_STYPE = 5;
    
    int POS_SYSTEMBYTES = 6;
    
    int getSessionId();
    
    long getTransactionId();
    
    byte[] toByteArray();
    
}
