package org.ozsoft.freecap;

public class Factory extends Enterprise {
    
    private int production;
    
    public Factory(String name, Product product, Company company, City city) {
        super(name, product, company, city);
        production = product.getBaseProduction();
    }
    
    public int getLevel() {
        return product.getLevel();
    }
    
    public int getProduction() {
        return production;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public void doNextTurn() {
        System.out.println(name + ": Processing next turn");
        produce();
    }
    
    private void produce() {
        System.out.format("%s: Producing %d units of %s\n", name, production, product);
        InventoryItem item = getInventoryItem(product);
        item.setCount(item.getCount() + production);
    }

}
