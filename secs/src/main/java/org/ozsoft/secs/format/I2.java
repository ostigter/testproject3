package org.ozsoft.secs.format;

/**
 * 2-byte signed integer (I2).
 * 
 * @author Oscar Stigter
 */
public class I2 extends IntegerBase {
    
    public static final int FORMAT_CODE = 0x64;
    
    private static final String NAME = "I2";
    
    private static final boolean IS_SIGNED = true;
    
    private static final int SIZE = 2;
    
    private static final int MIN_VALUE = -32768;
    
    private static final int MAX_VALUE = 32767;
    
    public I2() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    public I2(int value) {
        this();
        addValue(value);
    }
    
}
