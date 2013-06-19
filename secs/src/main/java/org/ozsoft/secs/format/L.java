package org.ozsoft.secs.format;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class L implements Data<List<Data<?>>> {
    
    private List<Data<?>> items = new ArrayList<Data<?>>();

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
    
    public void addItem(Data<?> item) {
        items.add(item);
    }
    
    public void clear() {
        items.clear();
    }

    @Override
    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] array = null;
        try {
            for (Data<?> item : items) {
                baos.write(item.toByteArray());
            }
            array = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not serialize L: " + e.getMessage());
        }
        return array;
    }

    @Override
    public String toSml() {
        StringBuilder sb = new StringBuilder();
        int length = items.size();
        sb.append(String.format("L {", length));
        for (int i = 0; i < length; i++) {
            sb.append('\n');
            sb.append(items.get(i).toSml());
        }
        sb.append("\n}");
        return sb.toString();
    }

}
