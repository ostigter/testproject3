package org.ozsoft.secs.format;

/**
 * 8-byte unsigned integer (U8).
 * 
 * @author Oscar Stigter
 */
public class U8 extends UBase {
    
    public static final int FORMAT_CODE = 0xa0;
    
    public static final int SIZE = 8;
    
    private static final long MIN_VALUE = 0x0000000000000000L;
    
    private static final long MAX_VALUE = 0x7fffffffffffffffL;
    
    public U8() {
        super("U8", FORMAT_CODE, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    public U8(long value) {
        this();
        addValue(value);
    }
    
    public U8(byte[] data) {
        this();
        addValue(data);
    }
    
}
