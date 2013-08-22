package org.ozsoft.secs;

/**
 * HSMS PType byte with the message protocol. <br />
 * <br />
 * 
 * Only the SECS_II value is supported.
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

    /**
     * Constructor.
     * 
     * @param value
     *            The value.
     */
    PType(int value) {
        this.value = value;
    }

    /**
     * Returns the value.
     * 
     * @return The value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Parses a value as PType.
     * 
     * @param value
     *            The value to parse.
     * @return The PType.
     */
    public static PType parse(int value) {
        for (PType pType : values()) {
            if (pType.value == value) {
                return pType;
            }
        }
        return PType.UNKNOWN;
    }

}
