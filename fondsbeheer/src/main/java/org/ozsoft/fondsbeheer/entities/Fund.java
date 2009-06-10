package org.ozsoft.fondsbeheer.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * A fund with its values.
 * 
 * @author Oscar Stigter
 */
public class Fund {
    
    /** The ID. */
	private String id;
    
	/** The name. */
	private String name;
    
	/** The values mapped by date. */
	private Map<SmallDate, FundValue> values;
    
    /**
	 * Constructor.
	 * 
	 * @param id
	 *            The ID.
	 * @param name
	 *            The name.
	 */
    public Fund(String id, String name) {
        this.id = id;
        this.name = name;
        this.values = new TreeMap<SmallDate, FundValue>();
    }
    
    /**
     * Returns the ID.
     * 
     * @return The ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the number of values.
     * 
     * @return The number of values.
     */
    public int getNoOfValues() {
        return values.size();
    }
    
    /**
     * Returns the values.
     * 
     * @return The values.
     */
    public Collection<FundValue> getValues() {
        return values.values();
    }
    
    /**
     * Adds a new value (only if no value for that date already exists).
     * 
     * @param fundValue The value.
     * 
     * @return True if added, otherwise false.
     */
    public boolean addValue(FundValue fundValue) {
    	SmallDate date = fundValue.getDate();
        if (!values.containsKey(date)) {
            values.put(date, fundValue);
            return true;
        } else {
            return false;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Fund) {
            return id.equals(((Fund) obj).getId());
        } else {
            return false;
        }
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return name; 
    }
    
    /**
	 * Serializes this fund to a byte stream.
	 * 
	 * Only the values are serialized.
	 * 
	 * @param dos
	 *            The output stream.
	 * 
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
    public void serialize(DataOutputStream dos) throws IOException {
    	dos.writeInt(values.size());
    	for (FundValue value : values.values()) {
    		value.serialize(dos);
    	}
    }
    
    /**
	 * Deserializes this fund from a byte stream.
	 * 
	 * Only the values are deserialized.
	 * 
	 * @param dis
	 *            The input stream.
	 * 
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
    public void deserialize(DataInputStream dis) throws IOException {
    	values.clear();
    	int noOfValues = dis.readInt();
    	for (int i = 0; i < noOfValues; i++) {
    		addValue(FundValue.deserialize(dis));
    	}
    }
    
}
