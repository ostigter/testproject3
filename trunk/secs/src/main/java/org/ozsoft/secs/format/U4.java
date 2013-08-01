package org.ozsoft.secs.format;

/**
 * 4-byte unsigned integer (U4).
 * 
 * @author Oscar Stigter
 */
public class U4 extends IntegerBase {
    
    public static final int SIZE = 4;
    
    public static final int FORMAT_CODE = 0xb0;
    
    private static final String NAME = "U4";
    
    private static final boolean IS_SIGNED = false;
    
    private static final long MIN_VALUE = 0x00000000L;
    
    private static final long MAX_VALUE = 0xffffffffL;
    
    public U4() {
        super(NAME, FORMAT_CODE, IS_SIGNED, SIZE, MIN_VALUE, MAX_VALUE);
    }
    
    public U4(long value) {
        this();
        addValue(value);
    }
    
    public U4(byte[] data) {
        this();
        addValue(data);
    }
    
}
