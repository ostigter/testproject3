package org.ozsoft.freecap;

public class Ingredient {
    
    private final Product product;
    
    private final int amount;
    
    public Ingredient(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public int getAmount() {
        return amount;
    }

}
