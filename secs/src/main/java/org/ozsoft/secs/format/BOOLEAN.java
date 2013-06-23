package org.ozsoft.secs.format;

public class BOOLEAN implements Data<Boolean> {
    
    public static final int FORMAT_CODE = 0x10;
    
    public static final int LENGTH = 1;
    
    public static final byte TRUE = 0x01;
    
    public static final byte FALSE = 0x00;
    
    public static final String TRUE_SML = "BOOLEAN {True}";
    
    public static final String FALSE_SML = "BOOLEAN {False}";
    
    private boolean value;
    
    public BOOLEAN(byte b) {
        setValue(b == FALSE ? false : true);
    }
    
    public BOOLEAN(int b) {
        setValue(b == FALSE ? false : true);
    }
    
    public BOOLEAN(boolean value) {
        setValue(value);
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public int length() {
        return LENGTH;
    }

    @Override
    public byte[] toByteArray() {
        return new byte[] {FORMAT_CODE, LENGTH, (value) ? TRUE : FALSE};
    }

    @Override
    public String toSml() {
        return value ? TRUE_SML : FALSE_SML;
    }
    
    @Override
    public String toString() {
        return toSml();
    }

}
