package org.ozsoft.freecap;

public enum Product {

    WHEAT("Wheat", ProductLevel.RAW, 100000, 1000, 0),

    FLOUR("Flour", ProductLevel.INTERMEDIATE, new Ingredient[] { new Ingredient(Product.WHEAT, 10) }, 10000, 100, 0),

    BREAD("Bread", ProductLevel.FINISHED, new Ingredient[] { new Ingredient(Product.FLOUR, 1) }, 1000, 100, 1),

    ;

    private final String name;

    private final ProductLevel level;

    private final Ingredient[] ingredients;

    private final int stockCapactiy;

    private final int baseProduction;

    private final int baseDemand;

    Product(String name, ProductLevel level, int stockCapacity, int baseProduction, int baseDemand) {
        this.name = name;
        this.level = level;
        this.ingredients = new Ingredient[0];
        this.stockCapactiy = stockCapacity;
        this.baseProduction = baseProduction;
        this.baseDemand = baseDemand;
    }

    Product(String name, ProductLevel level, Ingredient[] ingredients, int stockCapacity, int baseProduction,
            int baseDemand) {
        this.name = name;
        this.level = level;
        this.ingredients = ingredients;
        this.stockCapactiy = stockCapacity;
        this.baseProduction = baseProduction;
        this.baseDemand = baseDemand;
    }

    public String getName() {
        return name;
    }

    public ProductLevel getLevel() {
        return level;
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public int getStockCapacity() {
        return stockCapactiy;
    }

    public int getBaseProduction() {
        return baseProduction;
    }

    public int getBaseDemand() {
        return baseDemand;
    }

    @Override
    public String toString() {
        return name;
    }
}
