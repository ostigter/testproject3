package org.ozsoft.customui;

public class Dimension {
    
    private final int width;
    
    private final int height;
    
    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    @Override
    public String toString() {
        return String.format("(%d, %d)", width, height);
    }

}
