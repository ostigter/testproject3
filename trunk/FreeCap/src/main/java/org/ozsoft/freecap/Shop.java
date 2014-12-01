package org.ozsoft.freecap;

public class Shop extends Business {

    private final Product product;

    public Shop(BusinessType type, int id, Company company, City city) {
        super(type, id, company, city);
        product = type.getProduct();
    }

    public int getDemand() {
        return type.getProduct().getBaseDemand() * city.getPopulation();
    }

    @Override
    public void doNextTurn() {
        purchaseProducts();
        sellProducts();
    }

    private void purchaseProducts() {
        if (supplier != null) {
            Product product = getProduct();
            int amount = product.getStockCapacity() - getStock(product);
            amount = supplier.sellResource(product, amount);
            supplier.sellResource(product, amount);
            increaseStock(product, amount);
            double cost = amount * supplier.getPrice();
            pay(supplier, cost);
            System.out.format("%s purchased %d units of %s for $%,.2f from %s.\n", this, amount, product, cost, supplier);
        }
    }

    private void sellProducts() {
        // Determine how many products to be sold based on local demand and availability.
        int amount = getDemand();
        int available = getStock(product);
        if (available < amount) {
            amount = available;
        }

        // Sell products.
        decreaseStock(product, amount);
        double payment = amount * price;
        System.out.format("%s sells %d units of %s for $%,.2f.\n", this, amount, product, payment);
        receivePayment(payment);
    }
}
