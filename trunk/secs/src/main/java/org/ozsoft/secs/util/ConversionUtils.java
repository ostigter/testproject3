package org.ozsoft.secs.util;

/**
 * Generic conversion utilities.
 * 
 * @author Oscar Stigter
 */
public abstract class ConversionUtils {

    /**
     * Converts an integer into a byte array.
     * 
     * @param value
     *            The integer (long).
     * @param length
     *            The length of the byte array.
     * 
     * @return The byte array.
     */
    public static byte[] integerToBytes(long value, int length) {
        final byte[] data = new byte[length];
        for (int i = 0; i < length; i++) {
            data[i] = (byte) (value >> (length - i - 1) * 8);
        }
        return data;
    }
    
    public static long bytesToSignedInteger(byte[] data) {
        final int length = data.length;
        long value = 0L;
        for (int i = 0; i < length; i++) {
            value |= data[i] << (length - i - 1) * 8;
        }
        return value;
    }

    public static long bytesToUnsignedInteger(byte[] data) {
        final int length = data.length;
        long value = 0L;
        for (int i = 0; i < length; i++) {
            int b = data[i];
            if (b < 0) {
                b += 256;
            }
            value |= b << (length - i - 1) * 8;
//            if (i == (length - 1)) {
//                value += b;
//            } else {
//                for (int j = (length - i - 1); j > 0; j--) {
//                    value += b << 8;
//                }
//            }
        }
        return value;
    }

}
