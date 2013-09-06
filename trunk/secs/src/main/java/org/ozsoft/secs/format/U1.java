package org.ozsoft.secs.format;

/**
 * SECS data item U1 (sequence of 1-byte unsigned integers).
 * 
 * @author Oscar Stigter
 */
public class U1 extends IntegerBase {

    /** SECS format code. */
    public static final int FORMAT_CODE = 0xa4;

    /** SECS name. */
    private static final String NAME = "U1";

    /** Whether the integers are signed. */
    private static final boolean IS_SIGNED = false;

    /** Size of a single value in bytes. */
    public static final int SIZE = 1;

    /** Minimum value. */
    private static final int MIN_VALUE = 0x00;

    /** Maximum value. */
    private static final int MAX_VALUE = 0xff;

    /**
     * Constructor with an initial empty sequence.
     */
    public U1() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }

    /**
     * Constructor with an initial sequence with a single value.
     * 
     * @param value
     *            The value.
     */
    public U1(int value) {
        this();
        addValue(value);
    }

    /**
     * Constructor with an initial sequence with a single value, based on a raw byte buffer.
     * 
     * @param data
     *            The raw byte buffer containing a single value.
     */
    public U1(byte[] data) {
        this();
        addValue(data);
    }
    
}
