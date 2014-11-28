package org.ozsoft.freecap;

public class Factory extends Business {

    private int production;

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
        System.out.format("%s produced %d units of %s.\n", getName(), production, product);
        InventoryItem item = getInventoryItem(product);
        item.setCount(item.getCount() + production);
    }
}
