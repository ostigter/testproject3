package org.ozsoft.secs.format;

import java.util.ArrayList;
import java.util.List;

public class B implements Data<List<Integer>> {
    
    public static final int MIN_VALUE = 0x00;
    
    public static final int MAX_VALUE = 0xff;
    
    private List<Integer> bytes = new ArrayList<Integer>();
    
    public B() {
        // Empty implementation.
    }
    
    public B(byte[] data) {
        add(data);
    }
    
    @Override
    public List<Integer> getValue() {
        return bytes;
    }

    @Override
    public void setValue(List<Integer> bytes) {
        this.bytes = bytes;
    }
    
    @Override
    public int length() {
        return bytes.size();
    }
    
    public int get(int index) {
        return bytes.get(index);
    }
    
    public void add(byte b) {
        add((int) b);
    }
    
    public void add(int b) {
        if (b < MIN_VALUE || b > MAX_VALUE) {
            throw new IllegalArgumentException("Invalid value for B: " + b);
        }
        bytes.add(b);
    }
    
    public void add(byte[] data) {
        for (byte b : data) {
            add(b);
        }
    }
    
    public void add(int[] data) {
        for (int b : data) {
            add(b);
        }
    }
    
    public void add(B data) {
        int length = data.length();
        for (int i = 0; i < length; i++) {
            add(data.get(i));
        }
    }
    
    public void clear() {
        bytes.clear();
    }

    @Override
    public byte[] toByteArray() {
        int length = length();
        byte[] array = new byte[bytes.size()];
        for (int i = 0; i < length; i++) {
            array[i] = (byte) (bytes.get(i) & 0xff);
        }
        return array;
    }

    @Override
    public String toSml() {
        StringBuilder sb = new StringBuilder();
        int length = length();
        sb.append(String.format("B:%d", length));
        if (length > 0) {
            sb.append(" {");
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    sb.append(' ');
                }
                sb.append(String.format("%02x", bytes.get(i)));
            }
            sb.append('}');
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return toSml();
    }
}
