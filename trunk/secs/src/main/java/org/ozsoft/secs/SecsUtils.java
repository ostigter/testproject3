package org.ozsoft.secs;

public abstract class SecsUtils {
    
    public static final int U4_LENGTH = 4;
    
    public static int toU4(byte[] data) {
        if (data.length != U4_LENGTH) {
            throw new IllegalArgumentException("Invalid U4 length");
        }
        return (((int) (data[0] & 0x7F)) << 24) | (((int) (data[1] & 0xFF)) << 16) | (((int) (data[2] & 0xFF)) << 8) | (((int) (data[3] & 0xFF)));
    }
    
}
