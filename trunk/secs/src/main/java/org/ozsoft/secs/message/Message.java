package org.ozsoft.secs.message;

import org.ozsoft.secs.PType;
import org.ozsoft.secs.SType;
import org.ozsoft.secs.format.U2;
import org.ozsoft.secs.format.U4;

public abstract class Message {
    
    public static final int LENGTH_LENGTH = 4;
    
    public static final int HEADER_LENGTH = 10;
    
    public static final byte[] HEADER_LENGTH_BYTES = new byte[] {0, 0, 0, HEADER_LENGTH};
    
    public static final int MIN_LENGTH = LENGTH_LENGTH + HEADER_LENGTH;
    
    public static final long MAX_LENGTH = 256 * 1024; // 256 kB
    
    public static final int SESSION_ID_LENGTH = 2;
    
    public static final int SYSTEM_BYTES_LENGTH = 4;
    
    public static final int POS_SESSIONID = 0;
    
    public static final int POS_HEADERBYTE2 = 2;
    
    public static final int POS_HEADERBYTE3 = 3;
    
    public static final int POS_PTYPE = 4;
    
    public static final int POS_STYPE = 5;
    
    public static final int POS_SYSTEMBYTES = 6;
    
    private final U2 sessionId;
    
    private final int headerByte2;
    
    private final int headerByte3;
    
    private final PType pType;
    
    private final SType sType;
    
    private final U4 systemBytes;
    
    public Message(U2 sessionId, int headerByte2, int headerByte3, PType pType, SType sType, U4 systemBytes) {
        this.sessionId = sessionId;
        this.headerByte2 = headerByte2;
        this.headerByte3 = headerByte3;
        this.pType = pType;
        this.sType = sType;
        this.systemBytes = systemBytes;
    }
    
    public U2 getSessionId() {
        return sessionId;
    }
    
    public int getHeaderByte2() {
        return headerByte2;
    }
    
    public int getHeaderByte3() {
        return headerByte3;
    }
    
    public PType getPType() { 
        return pType;
    }
    
    public SType getSType() {
        return sType;
    }
    
    public U4 getSystemBytes() {
        return systemBytes;
    }
    
    public abstract byte[] toByteArray();
    
}
