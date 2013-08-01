package org.ozsoft.secs.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.util.ConversionUtils;

public class F4 implements Data<List<Float>> {
    
    public static final int FORMAT_CODE = 0x90;
    
    private static final String NAME = "F4";
    
    private static final int SIZE = 4;
    
    private List<Float> values = new ArrayList<Float>();
    
    public F4() {
        // Empty implementation.
    }
    
    public F4(float value) {
        addValue(value);
    }
    
    @Override
    public List<Float> getValue() {
        return values;
    }

    @Override
    public void setValue(List<Float> values) {
        this.values = values;
    }

    public float getValue(int index) {
        return values.get(index);
    }
    
    public void addValue(float value) {
        values.add(value);
    }

    public void addValue(byte[] data) {
        if (data.length != SIZE) {
            throw new IllegalArgumentException(String.format("Invalid %s length: %d bytes", NAME, data.length));
        }
        addValue(Float.intBitsToFloat((int) ConversionUtils.bytesToSignedInteger(data)));
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
                int bits = Float.floatToIntBits(values.get(i));
                baos.write(ConversionUtils.integerToBytes(bits, SIZE));
            }
            
            return baos.toByteArray();
            
        } catch (IOException e) {
            // This should never happen.
            throw new RuntimeException("Could not serialize data item", e);
            
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    @Override
    public String toSml() {
        int length = length();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s:%d", NAME, length));
        if (length > 0) {
            boolean first = true;
            sb.append(" {");
            for (float value : values) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof F4) {
            F4 f4 = (F4) obj;
            int length = f4.length();
            if (length == values.size()) {
                for (int i = 0; i < length; i++) {
                    if (f4.getValue(i) != values.get(i)) {
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
