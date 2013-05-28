package org.ozsoft.secs;

/**
 * Message type (SType).
 * 
 * @author Oscar Stigter
 */
public enum SType {
    
    /** DATA message. */
    DATA((byte) 0),
    
    /** SELECT_REQ message. */
    SELECT_REQ((byte) 1),
    
    /** SELECT_RESP message. */
    SELECT_RSP((byte) 2),
    
    /** DESELECT_REQ message. */
    DESELECT_REQ((byte) 3),
    
    /** DESELECT_RSP message. */
    DESELECT_RSP((byte) 4),
    
    /** LINKTEST_REQ message. */
    LINKTEST_REQ((byte) 5),
    
    /** LINKTEST_RSP message. */
    LINKTEST_RSP((byte) 6),
    
    /** REJECT message. */
    REJECT((byte) 7),
    
    /** SEPARATE message. */
    SEPARATE((byte) 9),
    
    /** Unknown message type. */
    UNKNOWN((byte) -1),
    
    ;
    
    private byte value;
    
    SType(byte value) {
        this.value = value;
    }
    
    public static SType parse(byte value) {
        for (SType pType : values()) {
            if (pType.value == value) {
                return pType;
            }
        }
        return SType.UNKNOWN;
    }

}
