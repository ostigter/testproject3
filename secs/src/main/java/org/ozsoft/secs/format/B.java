package org.ozsoft.secs.format;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class B implements Data<List<Integer>> {
    
    public static final int FORMAT_CODE = 0x20;
    
    public static final int MIN_VALUE = 0x00;
    
    public static final int MAX_VALUE = 0xff;
    
    private List<Integer> bytes = new ArrayList<Integer>();
    
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
//        if (b < MIN_VALUE || b > MAX_VALUE) {
//            throw new IllegalArgumentException("Invalid value for B: " + b);
//        }
        bytes.add((byte) b & 0xff);
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
            for (int i = 0; i < noOfLengthBytes; i++) {
                baos.write(lengthBytes.get(i));
            }
            // Write bytes recursively.
            for (int b : bytes) {
                baos.write(b);
            }
            return baos.toByteArray();
        } finally {
            IOUtils.closeQuietly(baos);
        }
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
    public boolean equals(Object obj) {
        if (obj instanceof B) {
            B b = (B) obj;
            int length = b.length();
            if (length == length()) {
                for (int i = 0; i < length; i++) {
                    if (b.get(i) != bytes.get(i)) {
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
        return toSml();
    }
}
