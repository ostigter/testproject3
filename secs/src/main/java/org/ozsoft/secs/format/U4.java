package org.ozsoft.secs.format;

/**
 * 2-byte unsigned integer (U2).
 * 
 * @author Oscar Stigter
 */
public class U4 {
    
    public static final int LENGTH = 4;
    
    private static final long MIN_VALUE = 0L;
    
    private static final long MAX_VALUE = 4294967296L;
    
    private long value;
    
    public U4() {
        this(0L);
    }
    
    public U4(long value) {
        setValue(value);
    }
    
    public U4(byte[] data) {
        setValue(data);
    }
    
    public long getValue() {
        return value;
    }
    
    public void setValue(long value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Invalid U4 value: " + value);
        }
        this.value = value;
    }
    
    public void setValue(byte[] data) {
        if (data.length != LENGTH) {
            throw new IllegalArgumentException("Invalid U4 length: " + data.length);
        }
        setValue((((int) (data[0] & 0x7F)) << 24) | (((int) (data[1] & 0xFF)) << 16) | (((int) (data[2] & 0xFF)) << 8) | (((int) (data[3] & 0xFF))));
    }
    
    public B toB() {
        byte b1 = (byte) (value >> 24);
        byte b2 = (byte) (value >> 16);
        byte b3 = (byte) (value >> 8);
        byte b4 = (byte) (value & 0xFF);
        return new B(new byte[] {b1, b2, b3, b4});
    }
    
    @Override
    public int hashCode() {
        return Long.valueOf(value).hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof U4) {
            return ((U4) obj).value == value;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return String.format("U4(%d)", value);
    }
    
}
