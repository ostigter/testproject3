package org.ozsoft.secs.format;

/**
 * 2-byte unsigned integer (U2).
 * 
 * @author Oscar Stigter
 */
public class U4 implements Data<Long> {
    
    public static final int LENGTH = 4;
    
    private static final long MIN_VALUE = 0x00000000L;
    
    private static final long MAX_VALUE = 0xffffffffL;
    
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
    
    @Override
    public Long getValue() {
        return value;
    }
    
    @Override
    public void setValue(Long value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Invalid U4 value: " + value);
        }
        this.value = value;
    }
    
    public void setValue(byte[] data) {
        if (data.length != LENGTH) {
            throw new IllegalArgumentException("Invalid U4 length: " + data.length);
        }
        setValue((((long) (data[0] & 0x7f)) << 24) | (((long) (data[1] & 0xff)) << 16) | (((long) (data[2] & 0xff)) << 8) | (((long) (data[3] & 0xff))));
    }
    
    @Override
    public int length() {
        return LENGTH;
    }
    
    @Override
    public byte[] toByteArray() {
        byte b1 = (byte) (value >> 24);
        byte b2 = (byte) (value >> 16);
        byte b3 = (byte) (value >> 8);
        byte b4 = (byte) (value & 0xff);
        return new byte[] {b1, b2, b3, b4};
    }

    @Override
    public String toSml() {
        return String.format("U4(%d)", value);
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
        return toSml();
    }

}
