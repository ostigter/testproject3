package org.ozsoft.secs.format;

/**
 * SECS data item I1 (sequence of 1-byte signed integers).
 * 
 * @author Oscar Stigter
 */
public class I1 extends IntegerBase {

    /** SECS format code. */
    public static final int FORMAT_CODE = 0x64;

    /** SECS name. */
    private static final String NAME = "I1";

    /** Whether the integers are signed. */
    private static final boolean IS_SIGNED = true;

    /** Size in bytes. */
    public static final int SIZE = 1;

    /** Minimum value. */
    private static final int MIN_VALUE = -128;

    /** Maximum value. */
    private static final int MAX_VALUE = 127;

    /**
     * Constructor with an initial empty sequence.
     */
    public I1() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }

    /**
     * Constructor with an initial sequence with a single value.
     * 
     * @param value
     *            The value.
     */
    public I1(int value) {
        this();
        addValue(value);
    }

    /**
     * Constructor with an initial sequence with a single value, based on a raw byte buffer.
     * 
     * @param data
     *            The raw byte buffer containing a single value.
     */
    public I1(byte[] data) {
        this();
        addValue(data);
    }
    
}
