package org.ozsoft.secs;

/**
 * Message protocol (PType).
 * 
 * @author Oscar Stigter
 */
public enum PType {
    
    /** SECS-II protocol. */
    SECS_II((byte) 0),
    
    /** Unknown protocol. */
    UNKNOWN((byte) -1),
    
    ;
    
    private byte value;
    
    PType(byte value) {
        this.value = value;
    }
    
    public static PType parse(byte value) {
        for (PType pType : values()) {
            if (pType.value == value) {
                return pType;
            }
        }
        return PType.UNKNOWN;
    }

}
