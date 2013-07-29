package org.ozsoft.secs;

/**
 * Message type (SType).
 * 
 * @author Oscar Stigter
 */
public enum SType {
    
    /** DATA message. */
    DATA(0),
    
    /** SELECT_REQ message. */
    SELECT_REQ(1),
    
    /** SELECT_RESP message. */
    SELECT_RSP(2),
    
    /** DESELECT_REQ message. */
    DESELECT_REQ(3),
    
    /** DESELECT_RSP message. */
    DESELECT_RSP(4),
    
    /** LINKTEST_REQ message. */
    LINKTEST_REQ(5),
    
    /** LINKTEST_RSP message. */
    LINKTEST_RSP(6),
    
    /** REJECT message. */
    REJECT(7),
    
    /** SEPARATE message. */
    SEPARATE(9),
    
    /** Unknown message type. */
    UNKNOWN(-1),
    
    ;
    
    private int value;
    
    SType(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static SType parse(int value) {
        for (SType pType : values()) {
            if (pType.value == value) {
                return pType;
            }
        }
        return SType.UNKNOWN;
    }

}
