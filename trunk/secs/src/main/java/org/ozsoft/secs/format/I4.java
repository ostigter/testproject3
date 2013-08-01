package org.ozsoft.secs.format;


/**
 * 4-byte signed integer (I4).
 * 
 * @author Oscar Stigter
 */
public class I4 extends IntegerBase {
    
    public static final int FORMAT_CODE = 0x70;
    
    private static final String NAME = "I4";
    
    private static final boolean IS_SIGNED = true;
    
    private static final int SIZE = 4;
    
    private static final int MIN_VALUE = -2147483647;
    
    private static final int MAX_VALUE = 2147483646;
    
    public I4() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    public I4(int value) {
        this();
        addValue(value);
    }
    
}
