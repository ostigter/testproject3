package org.ozsoft.secs.format;

import java.util.ArrayList;
import java.util.List;


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

    @Override
    public int length() {
        return values.size();
    }
    
    @Override
    public byte[] toByteArray() {
//        byte formatByte = (byte) (FORMAT_CODE | 0x01); // 1 length byte
//        byte lengthByte = values.size();
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
