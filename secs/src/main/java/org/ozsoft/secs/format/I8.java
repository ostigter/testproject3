package org.ozsoft.secs.format;

/**
 * 2-byte signed integer (I2).
 * 
 * @author Oscar Stigter
 */
public class I8 extends IntegerBase {
    
    public static final int FORMAT_CODE = 0x60;
    
    private static final String NAME = "I8";
    
    private static final boolean IS_SIGNED = true;
    
    private static final int SIZE = 8;
    
    private static final long MIN_VALUE = Long.MIN_VALUE;
    
    private static final long MAX_VALUE = Long.MAX_VALUE;
    
    public I8() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    public I8(long value) {
        this();
        addValue(value);
    }
    
}
