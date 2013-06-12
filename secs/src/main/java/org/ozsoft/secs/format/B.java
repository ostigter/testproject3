package org.ozsoft.secs.format;

import java.util.ArrayList;
import java.util.List;

/**
 * List of bytes.
 * 
 * @author Oscar Stigter
 */
public class B {
    
    private List<Integer> list = new ArrayList<Integer>();
    
    public B() {
        // Empty implementation.
    }
    
    public B(byte b) {
        add(b);
    }
    
    public B(int b) {
        add(b);
    }
    
    public B(byte[] data) {
        add(data);
    }
    
    public B(int[] data) {
        add(data);
    }
    
    public B(B b) {
        add(b);
    }
    
    public int length() {
        return list.size();
    }
    
    public int get(int index) {
        return list.get(index);
    }
    
    public void add(byte b) {
        list.add(b & 0xFF);
    }
    
    public void add(int b) {
        add((byte) (b & 0xFF));
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
    
    public void add(B b) {
        final int length = b.length();
        for (int i = 0; i < length; i++) {
            list.add(b.get(i));
        }
    }
    
    public void clear() {
        list.clear();
    }
    
    public byte[] toByteArray() {
        final int length = list.size();
        final byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = (byte) (list.get(i) & 0xFF);
        }
        return array;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof B) {
            final B b = (B) obj;
            final int length = b.length();
            if (length == length()) {
                for (int i = 0; i < length; i++) {
                    if (b.get(i) != get(i)) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final int length = list.size();
        sb.append(String.format("B:%d", length));
        if (length > 0) {
            sb.append(" {");
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    sb.append(' ');
                }
                sb.append(String.format("%02x", list.get(i)));
            }
            sb.append('}');
        }
        return sb.toString();
    }
    
}
