package org.ozsoft.secs.format;

/**
 * 2-byte unsigned integer (U2).
 * 
 * @author Oscar Stigter
 */
public class U2 extends UBase {
    
    public static final int FORMAT_CODE = 0xa8;
    
    public static final int MIN_LENGTH = 2;
    
    private static final int MIN_VALUE = 0x0000;
    
    private static final int MAX_VALUE = 0xffff;
    
    public U2() {
        super("U2", FORMAT_CODE, MIN_LENGTH, MIN_VALUE, MAX_VALUE);
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
