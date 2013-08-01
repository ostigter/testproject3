package org.ozsoft.secs.format;

/**
 * 2-byte unsigned integer (U1).
 * 
 * @author Oscar Stigter
 */
public class U1 extends UBase {
    
    public static final int FORMAT_CODE = 0xa4;
    
    public static final int SIZE = 1;
    
    private static final int MIN_VALUE = 0x00;
    
    private static final int MAX_VALUE = 0xff;
    
    public U1() {
        super("U1", FORMAT_CODE, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    public U1(int value) {
        this();
        addValue(value);
    }
    
    public U1(byte[] data) {
        this();
        addValue(data);
    }
    
}
