package org.ozsoft.secs;

/**
 * HSMS SType byte with the message type.
 * 
 * @author Oscar Stigter
 */
public enum SType {

    /** DATA message. */
    DATA(0x00),

    /** SELECT_REQ message. */
    SELECT_REQ(0x01),

    /** SELECT_RESP message. */
    SELECT_RSP(0x02),

    /** DESELECT_REQ message. */
    DESELECT_REQ(0x03),

    /** DESELECT_RSP message. */
    DESELECT_RSP(0x04),

    /** LINKTEST_REQ message. */
    LINKTEST_REQ(0x05),

    /** LINKTEST_RSP message. */
    LINKTEST_RSP(0x06),

    /** REJECT message. */
    REJECT(0x07),

    /** SEPARATE message. */
    SEPARATE(0x09),

    /** Unknown message type. */
    UNKNOWN(0xff),

    ;

    private int value;

    /**
     * Constructor.
     * 
     * @param value
     *            The value.
     */
    SType(int value) {
        this.value = value;
    }

    /**
     * Returns the SType value.
     * 
     * @return The SType value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Parse a value as SType.
     * 
     * @param value
     *            The value to parse.
     * 
     * @return The SType.
     */
    public static SType parse(int value) {
        for (SType pType : values()) {
            if (pType.value == value) {
                return pType;
            }
        }
        return SType.UNKNOWN;
    }

}
