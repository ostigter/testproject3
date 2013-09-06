package org.ozsoft.secs.format;

/**
 * SECS data item I8 (sequence of 8-byte signed integers).
 * 
 * @author Oscar Stigter
 */
public class I8 extends IntegerBase {
    
    /** SECS format code. */
    public static final int FORMAT_CODE = 0x60;
    
    /** SECS name. */
    private static final String NAME = "I8";
    
    /** Whether the integers are signed. */
    private static final boolean IS_SIGNED = true;
    
    /** Size in bytes. */
    public static final int SIZE = 8;
    
    /** Minimum value. */
    private static final long MIN_VALUE = Long.MIN_VALUE;
    
    /** Maximum value. */
    private static final long MAX_VALUE = Long.MAX_VALUE;
    
    /**
     * Constructor with an initial empty sequence.
     */
    public I8() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    /**
     * Constructor with an initial sequence with a single value.
     * 
     * @param value
     *            The value.
     */
    public I8(long value) {
        this();
        addValue(value);
    }
    
}
