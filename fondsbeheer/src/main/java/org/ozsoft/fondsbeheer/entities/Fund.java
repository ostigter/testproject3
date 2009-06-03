package org.ozsoft.fondsbeheer.entities;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * A fund.
 * 
 * @author Oscar Stigter
 */
public class Fund {
    
    private String id;
    
    private String name;
    
    private Map<SmallDate, FundValue> values;
    
    public Fund(String id, String name) {
        this.id = id;
        this.name = name;
        this.values = new TreeMap<SmallDate, FundValue>();
    }
    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public int getNoOfValues() {
        return values.size();
    }
    
    public Collection<FundValue> getValues() {
        return values.values();
    }
    
    public boolean addValue(FundValue fundValue) {
    	SmallDate date = fundValue.getDate();
        if (!values.containsKey(date)) {
            values.put(date, fundValue);
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Fund) {
            return id.equals(((Fund) obj).getId());
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return name; 
    }
    
    public void serialize(DataOutputStream dos) throws IOException {
    	dos.writeUTF(id);
    	dos.writeUTF(name);
    	dos.writeInt(values.size());
    	for (FundValue value : values.values()) {
    		value.serialize(dos);
    	}
    }
    
    public static Fund deserialize(DataInputStream dis) throws IOException {
    	String id = dis.readUTF();
    	String name = dis.readUTF();
    	int noOfValues = dis.readInt();
    	for (int i = 0; i < noOfValues; i++) {
    		
    	}
        return new SmallDate(day, month, year);
    }
    
}
