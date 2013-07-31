package org.ozsoft.secs.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.util.ConversionUtils;

/**
 * 8-byte unsigned integer (U8).
 * 
 * @author Oscar Stigter
 */
public class U8 implements Data<List<Long>> {
    
    public static final int FORMAT_CODE = 0xa0;
    
    public static final int MIN_LENGTH = 8;
    
    private static final long MIN_VALUE = 0x0000000000000000L;
    
    private static final long MAX_VALUE = 0x7fffffffffffffffL;
    
    private List<Long> values = new ArrayList<Long>();
    
    public U8() {
        // Empty implementation.
    }
    
    public U8(long value) {
        addValue(value);
    }
    
    public U8(byte[] data) {
        addValue(data);
    }
    
    @Override
    public List<Long> getValue() {
        return values;
    }
    
    @Override
    public void setValue(List<Long> value) {
        this.values = value;
    }
    
    public long getValue(int index) {
        return values.get(index);
    }
    
    public void addValue(long value) {
      if (value < MIN_VALUE || value > MAX_VALUE) {
          throw new IllegalArgumentException("Invalid U8 value: " + value);
      }
        this.values.add(value);
    }
    
    public void addValue(byte[] data) {
        if (data.length < MIN_LENGTH) {
            throw new IllegalArgumentException("Invalid U8 length: " + data.length);
        }
        addValue(ConversionUtils.bytesToUnsignedInteger(data));
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
                baos.write(ConversionUtils.integerToBytes(values.get(i), MIN_LENGTH));
            }
            
            return baos.toByteArray();
            
        } catch (IOException e) {
            // Internal error (should never happen).
            throw new RuntimeException("Could not serialize U8: " + e.getMessage());
            
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }
    
    public byte[] getBytes() {
      int length = length();
      byte[] data = new byte[length * MIN_LENGTH];
      for (int i = 0; i < length; i++) {
          byte[] valueBytes = ConversionUtils.integerToBytes(values.get(i), MIN_LENGTH);
          System.arraycopy(valueBytes, 0, data, i * MIN_LENGTH, MIN_LENGTH);
      }
      return data;
    }

    @Override
    public String toSml() {
        int length = length();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("U8:%d", length));
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
    
    //FIXME: Implement U8.hashCode()
    
    //FIXME: Enhance U8.equals()
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof U8) {
            return ((U8) obj).values == values;
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return toSml();
    }

}
