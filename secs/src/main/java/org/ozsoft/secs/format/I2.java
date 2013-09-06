package org.ozsoft.secs.format;

/**
 * SECS data item I2 (sequence of 2-byte signed integers).
 * 
 * @author Oscar Stigter
 */
public class I2 extends IntegerBase {
    
    /** SECS format code. */
    public static final int FORMAT_CODE = 0x68;
    
    /** SECS name. */
    private static final String NAME = "I2";
    
    /** Whether the integers are signed. */
    private static final boolean IS_SIGNED = true;
    
    /** Size in bytes. */
    public static final int SIZE = 2;
    
    /** Minimum value. */
    private static final int MIN_VALUE = -32768;
    
    /** Maximum value. */
    private static final int MAX_VALUE = 32767;
    
    /**
     * Constructor with an initial empty sequence.
     */
    public I2() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    /**
     * Constructor with an initial sequence with a single value.
     * 
     * @param value
     *            The value.
     */
    public I2(int value) {
        this();
        addValue(value);
    }
    
    /**
     * Constructor with an initial sequence with a single value, based on a raw byte buffer.
     * 
     * @param data
     *            The raw byte buffer containing a single value.
     */
    public I2(byte[] data) {
        this();
        addValue(data);
    }
    
}
