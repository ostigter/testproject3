package org.ozsoft.freecap;

public class Shop extends Business {

    private final Product product;

    private final int baseDemand;

    public Shop(BusinessType type, int id, Company company, City city) {
        super(type, id, company, city);
        product = type.getProduct();
        baseDemand = product.getBaseDemand();
    }

    public int getDemand() {
        return baseDemand * city.getPopulation();
    }

    @Override
    public void doNextTurn() {
        purchaseProducts();
        sellProducts();
    }

    private void purchaseProducts() {
        if (supplier != null) {
            Product product = getProduct();
            // FIXME: Determine actual amount of resources to purchase.
            int amount = 100;
            supplier.sellResource(product, amount);
            // TODO: Payment
            increaseStock(product, amount);
            System.out.format("%s purchased %d units of %s from %s.\n", this, amount, product, supplier);
        }
    }

    private void sellProducts() {
        // Determine how many products to be sold based on local demand and availability.
        int amountToSell = getDemand();
        int available = getStock(product);
        if (available < amountToSell) {
            amountToSell = available;
        }

        // Consume products.
        decreaseStock(product, amountToSell);
        System.out.format("%s sells %d units of %s.\n", this, amountToSell, product);
    }
}
