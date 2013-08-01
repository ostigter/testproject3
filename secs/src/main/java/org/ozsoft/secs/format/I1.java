package org.ozsoft.secs.format;

/**
 * 1-byte signed integer (I1).
 * 
 * @author Oscar Stigter
 */
public class I1 extends IntegerBase {
    
    public static final int FORMAT_CODE = 0x64;
    
    private static final String NAME = "I1";
    
    private static final boolean IS_SIGNED = true;
    
    public static final int SIZE = 1;
    
    private static final int MIN_VALUE = -128;
    
    private static final int MAX_VALUE = 127;
    
    public I1() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    public I1(int value) {
        this();
        addValue(value);
    }
    
}
