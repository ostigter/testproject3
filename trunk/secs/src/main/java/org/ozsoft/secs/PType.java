package org.ozsoft.secs;

/**
 * HSMS PType byte with the message protocol.
 * 
 * @author Oscar Stigter
 */
public enum PType {
    
    /** SECS-II protocol. */
    SECS_II(0x00),
    
    /** Unknown protocol. */
    UNKNOWN(0xff),
    
    ;
    
    private int value;
    
    PType(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public static PType parse(int value) {
        for (PType pType : values()) {
            if (pType.value == value) {
                return pType;
            }
        }
        return PType.UNKNOWN;
    }

}
