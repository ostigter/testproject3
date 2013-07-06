package org.ozsoft.secs.format;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;


/**
 * 1-byte signed integer (I1).
 * 
 * @author Oscar Stigter
 */
public class I1 implements Data<List<Integer>> {
    
    public static final int FORMAT_CODE = 0x64;
    
    public static final int MIN_LENGTH = 1;
    
    private static final int MIN_VALUE = -128;
    
    private static final int MAX_VALUE = 127;
    
    private List<Integer> values = new ArrayList<Integer>();
    
    public I1() {
        // Empty implementation.
    }
    
    public I1(int value) {
        addValue(value);
    }
    
    public I1(byte[] data) {
        if (data.length < MIN_LENGTH) {
            throw new IllegalArgumentException("Invalid I1 length: " + data.length);
        }
        addValue(data[0] & 0xff);
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
            throw new IllegalArgumentException("Invalid I1 value: " + value);
        }
        values.add(value);
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
                baos.write(values.get(i));
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
            array[i] = (byte) (values.get(i) & 0xff);
        }
        return array;
    }

    @Override
    public String toSml() {
        int length = length();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("I1:%d", length));
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

    //FIXME: Implement I1.hashCode()
    
    //FIXME: Enhance I1.equals()
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof I1) {
            return ((I1) obj).values == values;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return toSml();
    }
    
}
