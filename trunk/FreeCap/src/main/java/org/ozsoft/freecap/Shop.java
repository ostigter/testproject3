package org.ozsoft.freecap;

public class Shop extends Business {

    private final Product product;

    private final int demand;

    public Shop(BusinessType type, int id, Company company, City city) {
        super(type, id, company, city);
        product = type.getProduct();
        demand = product.getBaseDemand();
    }

    public int getDemand() {
        return demand;
    }

    @Override
    public void doNextTurn() {
        sell();
    }

    private void sell() {
        // Determine how many products to be sold based on local demand and availability.
        int amountToSell = getDemand();
        int available = getInventory(product);
        if (available < amountToSell) {
            amountToSell = available;
            System.out.format("WARNING: %s selling %s below demand due to stock shortage.\n", this, product);
        }

        // Consume products.
        decreaseInventory(product, amountToSell);
        System.out.format("%s sells %d units of %s.\n", this, amountToSell, product);
    }
}
