package org.ozsoft.secs.format;

import java.io.ByteArrayOutputStream;

import org.apache.commons.io.IOUtils;

public class A implements Data<String> {
    
    public static final int FORMAT_CODE = 0x40;
    
    private String value;
    
    public A() {
        this("");
    }
    
    public A(String value) {
        setValue(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public int length() {
        return value.length();
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
            for (int b : value.getBytes()) {
                baos.write(b);
            }
            return baos.toByteArray();
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    @Override
    public String toSml() {
        return String.format("A:%d {'%s'}", value.length(), value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof A) {
            return ((A) obj).value.equals(value);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return toSml();
    }

}
