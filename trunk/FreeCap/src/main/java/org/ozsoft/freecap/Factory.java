package org.ozsoft.freecap;

public class Factory extends Business {

    private final int production;

    public Factory(BusinessType type, int id, Product product, Company company, City city) {
        super(type, id, product, company, city);
        production = product.getBaseProduction();
    }

    public int getProduction() {
        return production;
    }

    @Override
    public void doNextTurn() {
        produce();
    }

    private void produce() {
        // Determine how many products can be produced based on available resources.
        int amountToProduce = getProduction();
        for (Product ingredient : product.getIngredients().keySet()) {
            int capacity = getInventory(ingredient) / product.getIngredients().get(ingredient);
            if (capacity < amountToProduce) {
                amountToProduce = capacity;
            }
        }

        // Consume resources and create products.
        for (Product ingredient : product.getIngredients().keySet()) {
            decreaseInventory(ingredient, amountToProduce * product.getIngredients().get(ingredient));
        }
        increaseInventory(product, amountToProduce);
        System.out.format("%s produces %d units of %s.\n", getName(), amountToProduce, product);
        if (amountToProduce < getProduction()) {
            System.out.format("WARNING: %s producing below capacity due to lack of resources.\n", getName());
        }
    }
}
