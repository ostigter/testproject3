package org.ozsoft.secs.format;

public class A implements Data<String> {
    
    private String value;
    
    public A() {
        this("");
    }
    
    public A(String value) {
        setValue(value);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public int length() {
        return value.length();
    }

    @Override
    public byte[] toByteArray() {
        //TODO
        return new byte[] {};
    }

    @Override
    public String toSml() {
        return String.format("A:%d {'%s'}", value.length(), value);
    }
    
    @Override
    public String toString() {
        return toSml();
    }

}
