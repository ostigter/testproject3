package org.ozsoft.secs.format;


/**
 * 4-byte signed integer (I4).
 * 
 * @author Oscar Stigter
 */
public class I4 extends IBase {
    
    public static final int FORMAT_CODE = 0x70;
    
    public static final int LENGTH = 4;
    
    private static final int MIN_VALUE = -2147483647;
    
    private static final int MAX_VALUE = 2147483646;
    
    public I4() {
        super("I4", FORMAT_CODE, LENGTH, MIN_VALUE, MAX_VALUE);
    }
    
    public I4(int value) {
        this();
        addValue(value);
    }
    
}
