package org.ozsoft.secs.format;

/**
 * 2-byte unsigned integer (U2).
 * 
 * @author Oscar Stigter
 */
public class U2 extends IntegerBase {
    
    public static final int FORMAT_CODE = 0xa8;
    
    private static final String NAME = "U2";
    
    private static final boolean IS_SIGNED = false;
    
    public static final int SIZE = 2;
    
    private static final int MIN_VALUE = 0x0000;
    
    private static final int MAX_VALUE = 0xffff;
    
    public U2() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    public U2(int value) {
        this();
        addValue(value);
    }
    
    public U2(byte[] data) {
        this();
        addValue(data);
    }
    
}
