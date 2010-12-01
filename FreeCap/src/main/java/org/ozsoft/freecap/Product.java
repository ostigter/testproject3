package org.ozsoft.freecap;

import java.util.HashMap;
import java.util.Map;

public class Product {
    
    private final String name;
    
    private final ProductLevel level;
    
    private final Map<Product, Integer> ingredients;
    
    private final int baseProduction;
    
    public Product(String name, ProductLevel level, int baseProduction) {
        this.name = name;
        this.level = level;
        this.ingredients = new HashMap<Product, Integer>();
        this.baseProduction = baseProduction;
    }
    
    public String getName() {
        return name;
    }
    
    public ProductLevel getLevel() {
        return level;
    }
    
    public Map<Product, Integer> getIngredients() {
        return ingredients;
    }
    
    public void addIngredient(Product product, int amount) {
        ingredients.put(product, amount);
    }
    
    public int getBaseProduction() {
        return baseProduction;
    }
    
    @Override
    public String toString() {
        return name;
    }

}
