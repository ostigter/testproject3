package org.ozsoft.secs.format;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.util.ConversionUtils;


/**
 * 2-byte unsigned integer (U2).
 * 
 * @author Oscar Stigter
 */
public class U2 implements Data<List<Integer>> {
    
    public static final int FORMAT_CODE = 0xa8;
    
    public static final int MIN_LENGTH = 2;
    
    private static final int MIN_VALUE = 0x00;
    
    private static final int MAX_VALUE = 0xffff;
    
    private List<Integer> values = new ArrayList<Integer>();
    
    public U2() {
        // Empty implementation.
    }
    
    public U2(int value) {
        addValue(value);
    }
    
    public U2(byte[] data) {
        if (data.length < MIN_LENGTH) {
            throw new IllegalArgumentException("Invalid U2 length: " + data.length);
        }
        addValue((data[0] & 0xff) << 8 | (data[1] & 0xff));
    }
    
    @Override
    public List<Integer> getValue() {
        return values;
    }
    
    @Override
    public void setValue(List<Integer> values) {
        this.values = values;
    }
    
    public int getValue(int index) {
        return values.get(index);
    }
    
    public void addValue(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException("Invalid U2 value: " + value);
        }
        values.add(value);
    }
    
    public void addValue(byte[] data) {
        if (data.length < MIN_LENGTH) {
            throw new IllegalArgumentException("Invalid U2 length: " + data.length);
        }
        addValue((int) ConversionUtils.bytesToUnsignedInteger(data));
    }

    @Override
    public int length() {
        return values.size();
    }
    
    @Override
    public byte[] toByteArray() {
        // Determine length.
        int length = length();
        int noOfLengthBytes = 1;
        B lengthBytes = new B();
        lengthBytes.add(length & 0xff);
        if (length > 0xff) {
            noOfLengthBytes++;
            lengthBytes.add((length >> 8) & 0xff);
        }
        if (length > 0xffff) {
            noOfLengthBytes++;
            lengthBytes.add((length >> 16) & 0xff);
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // Write format byte.
            baos.write(FORMAT_CODE | noOfLengthBytes);
            
            // Write length bytes.
            for (int i = 0; i < noOfLengthBytes; i++) {
                baos.write(lengthBytes.get(i));
            }
            // Write values.
            for (int i = 0; i < length; i++) {
                int value = values.get(i);
                baos.write(value >> 8);
                baos.write(value & 0xff);
            }
            
            return baos.toByteArray();
            
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }
    
    public byte[] getBytes() {
        int length = length();
        byte[] array = new byte[length * MIN_LENGTH];
        for (int i = 0; i < length; i++) {
            int b = values.get(i);
            array[i * MIN_LENGTH] = (byte) (b >> 8);
            array[i * MIN_LENGTH + 1] = (byte) (b & 0xff);
        }
        return array;
    }

    @Override
    public String toSml() {
        int length = length();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("U2:%d", length));
        if (length > 0) {
            boolean first = true;
            sb.append(" {");
            for (int value : values) {
                if (!first) {
                    sb.append(' ');
                } else {
                    first = false;
                }
                sb.append(value);
            }
            sb.append('}');
        }
        return sb.toString();
    }

    //FIXME: Implement U2.hashCode()
    
    //FIXME: Enhance U2.equals()
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof U2) {
            return ((U2) obj).values == values;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return toSml();
    }
    
}
