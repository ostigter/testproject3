package org.ozsoft.secs.format;

/**
 * SECS data item U8 (sequence of 8-byte unsigned integers).
 * 
 * @author Oscar Stigter
 */
public class U8 extends IntegerBase {
    
    /** Size of a value in bytes. */
    public static final int SIZE = 8;
    
    /** SECS format code. */
    public static final int FORMAT_CODE = 0xa0;
    
    /** SECS name. */
    private static final String NAME = "U8";
    
    /** Whether the integers are signed. */
    private static final boolean IS_SIGNED = false;
    
    /** Minumum value. */
    private static final long MIN_VALUE = 0x0000000000000000L;
    
    /** Maximum value. */
    private static final long MAX_VALUE = 0x7fffffffffffffffL;
    
    /**
     * Constructor with an initial empty sequence.
     */
    public U8() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    /**
     * Constructor with an initial sequence with a single value.
     * 
     * @param value
     *            The value.
     */
    public U8(long value) {
        this();
        addValue(value);
    }
    
    /**
     * Constructor with an initial sequence with a single value, based on a raw byte buffer.
     * 
     * @param data
     *            The raw byte buffer containing a single value.
     */
    public U8(byte[] data) {
        this();
        addValue(data);
    }
    
}
