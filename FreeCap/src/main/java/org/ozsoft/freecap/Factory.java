package org.ozsoft.freecap;

public class Factory extends Business {

    private final int baseProduction;

    public Factory(BusinessType type, int id, Company company, City city) {
        super(type, id, company, city);
        baseProduction = getProduct().getBaseProduction();
    }

    public int getProduction() {
        return baseProduction;
    }

    @Override
    public void doNextTurn() {
        produce();
    }

    private void produce() {
        Product product = getProduct();

        // Determine how many products can be produced based on available resources.
        int amountToProduce = getProduction();
        for (Ingredient ingredient : product.getIngredients()) {
            int capacity = getInventory(ingredient.getProduct()) / ingredient.getAmount();
            if (capacity < amountToProduce) {
                amountToProduce = capacity;
            }
        }

        // Consume resources and create products.
        for (Ingredient ingredient : product.getIngredients()) {
            decreaseInventory(ingredient.getProduct(), amountToProduce * ingredient.getAmount());
        }
        increaseInventory(product, amountToProduce);
        System.out.format("%s produces %d units of %s.\n", getName(), amountToProduce, product);
        if (amountToProduce < getProduction()) {
            System.out.format("WARNING: %s producing below capacity due to lack of resources.\n", getName());
        }
    }
}
