package org.ozsoft.secs.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.ozsoft.secs.util.ConversionUtils;

/**
 * SECS data item F8 (sequence of 8-byte, double-precision floating-point
 * numbers).
 * 
 * @author Oscar Stigter
 */
public class F8 implements Data<List<Double>> {

    /** SECS name. */
    public static final String NAME = "F8";

    /** SECS format code. */
    public static final int FORMAT_CODE = 0x80;

    /** Fixed size in bytes. */
    public static final int SIZE = 8;

    /** The floating-point numbers. */
    private List<Double> values = new ArrayList<Double>();

    /**
     * Constructor with an initial empty sequence.
     */
    public F8() {
        // Empty implementation.
    }

    /**
     * Constructor with an initial single value.
     * 
     * @param value
     *            The value.
     */
    public F8(double value) {
        addValue(value);
    }

    /**
     * Constructor with an initial sequence with a single value, based on a raw byte buffer.
     * 
     * @param data
     *            The raw byte buffer containing a single value.
     */
    public F8(byte[] data) {
        this();
        addValue(data);
    }
    
    /**
     * Returns the value at a specific index position.
     * 
     * @param index
     *            The index position.
     * 
     * @return The value.
     */
    public double getValue(int index) {
        return values.get(index);
    }

    /**
     * Adds a value to the end of the sequence.
     * 
     * @param value
     *            The value to add.
     */
    public void addValue(double value) {
        values.add(value);
    }

    /**
     * Adds a value to the end of the sequence, based on a raw byte buffer.
     * 
     * @param data
     *            The byte buffer containing a single value.
     */
    public void addValue(byte[] data) {
        if (data.length != SIZE) {
            throw new IllegalArgumentException(String.format("Invalid %s length: %d bytes", NAME, data.length));
        }
        addValue(Double.longBitsToDouble(ConversionUtils.bytesToUnsignedInteger(data)));
    }

    @Override
    public List<Double> getValue() {
        return values;
    }

    @Override
    public void setValue(List<Double> values) {
        this.values = values;
    }

    @Override
    public int length() {
        return values.size();
    }

    @Override
    public byte[] toByteArray() {
        // Determine length.
        int length = values.size() * SIZE;
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
            for (double value : values) {
                long bits = Double.doubleToLongBits(value);
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
            for (double value : values) {
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
        if (obj instanceof F8) {
            F8 f8 = (F8) obj;
            int length = f8.length();
            if (length == values.size()) {
                for (int i = 0; i < length; i++) {
                    if (f8.getValue(i) != values.get(i)) {
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
