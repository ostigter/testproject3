package org.ozsoft.secs.format;

/**
 * SECS data item U4 (sequence of 4-byte unsigned integers).
 * 
 * @author Oscar Stigter
 */
public class U4 extends IntegerBase {
    
    /** Size of the integers in bytes. */
    public static final int SIZE = 4;
    
    /** SECS format code. */
    public static final int FORMAT_CODE = 0xb0;
    
    /** SECS name. */
    private static final String NAME = "U4";
    
    /** Whether the integers are signed. */
    private static final boolean IS_SIGNED = false;
    
    /** Minimum value. */
    private static final long MIN_VALUE = 0x00000000L;
    
    /** Maximum value. */
    private static final long MAX_VALUE = 0xffffffffL;
    
    /**
     * Constructor with an initial empty sequence.
     */
    public U4() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    /**
     * Constructor with an initial sequence with a single value.
     * 
     * @param value
     *            The value.
     */
    public U4(long value) {
        this();
        addValue(value);
    }
    
    /**
     * Constructor with an initial sequence with a single value, based on a raw byte buffer.
     * 
     * @param data
     *            The raw byte buffer containing a single value.
     */
    public U4(byte[] data) {
        this();
        addValue(data);
    }
    
}
