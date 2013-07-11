package org.ozsoft.secs.format;

/**
 * 1-byte signed integer (I1).
 * 
 * @author Oscar Stigter
 */
public class I1 extends IBase {
    
    public static final int FORMAT_CODE = 0x64;
    
    private static final int LENGTH = 1;
    
    private static final int MIN_VALUE = -128;
    
    private static final int MAX_VALUE = 127;
    
    public I1() {
        super("I1", FORMAT_CODE, LENGTH, MIN_VALUE, MAX_VALUE);
    }
    
    public I1(int value) {
        this();
        addValue(value);
    }
    
}
