package org.ozsoft.secs.format;

/**
 * SECS data item U2 (sequence of 2-byte unsigned integers).
 * 
 * @author Oscar Stigter
 */
public class U2 extends IntegerBase {
    
    /** Size of the integers in bytes. */
    public static final int SIZE = 2;
    
    /** SECS format code. */
    public static final int FORMAT_CODE = 0xa8;
    
    /** SECS name. */
    private static final String NAME = "U2";
    
    /** Whether the integers are signed. */
    private static final boolean IS_SIGNED = false;
    
    /** Minimum value. */
    private static final int MIN_VALUE = 0x0000;
    
    /** Maximum value. */
    private static final int MAX_VALUE = 0xffff;
    
    /**
     * Constructor with an initial empty sequence.
     */
    public U2() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    /**
     * Constructor with an initial sequence with a single value.
     * 
     * @param value
     *            The value.
     */
    public U2(int value) {
        this();
        addValue(value);
    }
    
    /**
     * Constructor with an initial sequence with a single value, based on a raw byte buffer.
     * 
     * @param data
     *            The raw byte buffer containing a single value.
     */
    public U2(byte[] data) {
        this();
        addValue(data);
    }
    
}
