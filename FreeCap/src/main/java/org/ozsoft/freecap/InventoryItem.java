package org.ozsoft.freecap;

public class InventoryItem {
    
    private final Product product;
    
    private int count = 0;
    
    public InventoryItem(Product product) {
        this.product = product;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    @Override
    public String toString() {
        return String.format("%s: %d", product, count);
    }
}
