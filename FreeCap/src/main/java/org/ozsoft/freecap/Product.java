package org.ozsoft.freecap;

public class Product {
    
    private final String name;
    
    private final Product ingredient;
    
    private final int baseProduction; 
    
    public Product(String name, Product ingredient, int baseProduction) {
        this.name = name;
        this.ingredient = ingredient;
        this.baseProduction = baseProduction;
    }
    
    public String getName() {
        return name;
    }
    
    public Product getIngredient() {
        return ingredient;
    }
    
    public int getLevel() {
        int level = 1;
        if (ingredient != null) {
            level += ingredient.getLevel();
        }
        return level;
    }
    
    public int getBaseProduction() {
        return baseProduction;
    }
    
    @Override
    public String toString() {
        return name;
    }

}
