package org.ozsoft.secs.format;

/**
 * 2-byte signed integer (I2).
 * 
 * @author Oscar Stigter
 */
public class I2 extends IBase {
    
    public static final int FORMAT_CODE = 0x64;
    
    private static final int LENGTH = 2;
    
    private static final int MIN_VALUE = -32768;
    
    private static final int MAX_VALUE = 32767;
    
    public I2() {
        super("I2", FORMAT_CODE, LENGTH, MIN_VALUE, MAX_VALUE);
    }
    
    public I2(int value) {
        this();
        addValue(value);
    }
    
}
