package org.ozsoft.secs.format;

public interface Data<T> {
    
    T getValue();
    
    void setValue(T value);
    
    int length();
    
    byte[] toByteArray();
    
    String toSml();

}
