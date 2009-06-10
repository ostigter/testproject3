package org.ozsoft.fondsbeheer.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Locale;

public class FundValue {
    
    private static final long serialVersionUID = 1L;

    private SmallDate date;
    
    private float price;
    
    public FundValue(SmallDate date, float price) {
        this.date = date;
        this.price = price;
    }
    
    public SmallDate getDate() {
        return date;
    }
    
    public float getPrice() {
        return price;
    }
    
    @Override
    public int hashCode() {
    	return date.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FundValue) {
            return date.equals(((FundValue) obj).getDate());
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return String.format(Locale.US, "{%s, %.2f}", date, price); 
    }
    
    public void serialize(DataOutputStream dos) throws IOException {
    	date.serialize(dos);
    	dos.writeFloat(price);
    }
    
    public static FundValue deserialize(DataInputStream dis) throws IOException {
    	SmallDate date = SmallDate.deserialize(dis);
    	float price = dis.readFloat();
    	return new FundValue(date, price);
    }
    
}
