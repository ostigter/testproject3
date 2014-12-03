package org.ozsoft.freecap;

import static org.ozsoft.freecap.Utils.money;

public class Factory extends Business {

    public Factory(BusinessType type, int id, Company company, City city) {
        super(type, id, company, city);
    }

    public int getProduction() {
        return type.getProduct().getBaseProduction();
    }

    @Override
    public void doNextTurn() {
        purchaseResources();
        produceProducts();
        payWeeklyCosts();
    }

    private void purchaseResources() {
        if (supplier != null) {
            Product product = getProduct();
            for (Ingredient ingredient : product.getIngredients()) {
                int amount = ingredient.getProduct().getStockCapacity() - getStock(ingredient.getProduct());
                amount = supplier.sellResource(ingredient.getProduct(), amount);
                increaseStock(ingredient.getProduct(), amount);
                double cost = amount * supplier.getSalesPrice();
                pay(supplier, cost);
                System.out.format("%s purchased %,d units of %s for %s from %s.\n", this, amount, ingredient.getProduct(), money(cost), supplier);
            }
        }
    }

    private void produceProducts() {
        Product product = getProduct();

        // Determine how many products can be produced based on available resources.
        int amountToProduce = getProduction();
        for (Ingredient ingredient : product.getIngredients()) {
            int capacity = getStock(ingredient.getProduct()) / ingredient.getAmount();
            if (capacity < amountToProduce) {
                amountToProduce = capacity;
            }
        }

        // Consume resources and create products.
        for (Ingredient ingredient : product.getIngredients()) {
            decreaseStock(ingredient.getProduct(), amountToProduce * ingredient.getAmount());
        }
        increaseStock(product, amountToProduce);
        System.out.format("%s produces %,d units of %s.\n", getName(), amountToProduce, product);
        if (amountToProduce < getProduction()) {
            System.out.format("WARNING: %s producing below capacity due to lack of resources.\n", getName());
        }
    }
}
