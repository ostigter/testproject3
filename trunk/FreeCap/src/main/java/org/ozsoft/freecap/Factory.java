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
        purchaseResources();
        produceProducts();
    }

    private void purchaseResources() {
        if (supplier != null) {
            Product product = getProduct();
            for (Ingredient ingredient : product.getIngredients()) {
                // FIXME: Determine actual amount of resources to purchase.
                int amount = 1000;
                supplier.sellResource(ingredient.getProduct(), amount);
                // TODO: Payment
                increaseStock(ingredient.getProduct(), amount);
                System.out.format("%s purchased %d units of %s from %s.\n", this, amount, ingredient.getProduct(),
                        supplier);
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
        System.out.format("%s produces %d units of %s.\n", getName(), amountToProduce, product);
        if (amountToProduce < getProduction()) {
            System.out.format("WARNING: %s producing below capacity due to lack of resources.\n", getName());
        }
    }
}
