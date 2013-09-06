package org.ozsoft.secs.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * SECS data item L (list of other data items).
 * 
 * @author Oscar Stigter
 */
public class L implements Data<List<Data<?>>> {
    
    /** SECS format code. */
    public static final int FORMAT_CODE = 0x00;
    
    /** The data items. */
    private List<Data<?>> items = new ArrayList<Data<?>>();
    
    /**
     * Constructor with an initial empty list.
     */
    public L() {
        // Empty implementation.
    }

    /**
     * Returns the data item at a specific index position.
     * 
     * @param index
     *            The index position.
     * 
     * @return The data item.
     */
    public Data<?> getItem(int index) {
        return items.get(index);
    }
    
    /**
     * Adds a data item to the end of the list.
     * 
     * @param item
     *            The data item.
     */
    public void addItem(Data<?> item) {
        items.add(item);
    }
    
    @Override
    public List<Data<?>> getValue() {
        return items;
    }

    @Override
    public void setValue(List<Data<?>> value) {
        this.items = value;
    }
    
    @Override
    public int length() {
        return items.size();
    }
    
    @Override
    public byte[] toByteArray() {
        // Construct length bytes.
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
        
            // Write items recursively.
            for (Data<?> item : items) {
                baos.write(item.toByteArray());
            }
            
            return baos.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Could not serialize L items: " + e.getMessage());
            
        } finally {
            IOUtils.closeQuietly(baos);
        }
    }

    @Override
    public String toSml() {
        StringBuilder sb = new StringBuilder();
        int length = items.size();
        sb.append(String.format("L:%d {", length));
        for (int i = 0; i < length; i++) {
            sb.append('\n');
            sb.append(items.get(i).toSml());
        }
        sb.append("\n}");
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return toSml();
    }

}
