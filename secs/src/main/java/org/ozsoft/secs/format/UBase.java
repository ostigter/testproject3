package org.ozsoft.secs.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.util.ConversionUtils;

public class UBase implements Data<List<Long>> {
    
    private List<Long> values = new ArrayList<Long>();
    
    protected String name;
    
    protected int formatCode;
    
    protected int size;
    
    protected long minValue;
    
    protected long maxValue;
    
    public UBase(String name, int formatCode, int size, long minValue, long maxValue) {
        this.name = name;
        this.formatCode = formatCode;
        this.size = size;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    
    @Override
    public List<Long> getValue() {
        return values;
    }

    @Override
    public void setValue(List<Long> values) {
        this.values = values;
    }

    public long getValue(int index) {
        return values.get(index);
    }
    
    public void addValue(long value) {
        if (value < minValue || value > maxValue) {
            throw new IllegalArgumentException("Invalid value: " + value);
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
            baos.write(formatCode | noOfLengthBytes);
            
            // Write length bytes.
            for (int i = 0; i < noOfLengthBytes; i++) {
                baos.write(lengthBytes.get(i));
            }
            
            // Write values.
            for (int i = 0; i < length; i++) {
                baos.write(ConversionUtils.integerToBytes(values.get(i), size));
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
        sb.append(String.format("%s:%d", name, length));
        if (length > 0) {
            boolean first = true;
            sb.append(" {");
            for (long value : values) {
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
    
    //FIXME: Enhance IBase.equals()
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UBase) {
            return ((UBase) obj).values == values;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return toSml();
    }
    
}
