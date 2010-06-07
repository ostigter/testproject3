package org.ozsoft.fondsbeheer.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Locale;

/**
 * The monetary value of a fund for a specific date.
 * 
 * @author Oscar Stigter
 */
public class FundValue {
    
	/** The date. */
	private SmallDate date;
    
	/** The value. */
	private float value;
    
	/**
	 * Constructor.
	 * 
	 * @param date
	 *            The date.
	 * @param value
	 *            The value.
	 */
	public FundValue(SmallDate date, float value) {
        this.date = date;
        this.value = value;
    }
    
	/**
	 * Returns the date.
	 * 
	 * @return The date.
	 */
	public SmallDate getDate() {
        return date;
    }
    
    /**
     * Returns the value.
     * 
     * @return The value.
     */
	public float getValue() {
        return value;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
    	return date.hashCode();
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FundValue) {
            return date.equals(((FundValue) obj).getDate());
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
        return String.format(Locale.US, "%s: %.3f", date, value); 
    }
    
    /**
	 * Serialize this fund value to an output stream.
	 * 
	 * @param dos
	 *            The output stream.
	 * 
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
    public void serialize(DataOutputStream dos) throws IOException {
    	date.serialize(dos);
    	dos.writeFloat(value);
    }
    
    /**
	 * Deserializes a fund from an input stream.
	 * 
	 * @param dis
	 *            The input stream.
	 * 
	 * @return The fund.
	 * 
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
    public static FundValue deserialize(DataInputStream dis) throws IOException {
    	SmallDate date = SmallDate.deserialize(dis);
    	float value = dis.readFloat();
    	return new FundValue(date, value);
    }
    
}
