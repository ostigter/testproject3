package org.ozsoft.freecap;

import java.util.HashMap;
import java.util.Map;

public abstract class Business implements GameListener {

    protected final BusinessType type;

    protected final int id;

    protected final Company company;

    protected final City city;

    protected final Map<Product, Integer> inventory;

    public Business(BusinessType type, int id, Company company, City city) {
        this.type = type;
        this.id = id;
        this.company = company;
        this.city = city;
        inventory = new HashMap<Product, Integer>();
    }

    public String getName() {
        return String.format("%s '%s%02d'", type.getName(), type.getPrefix(), id);
    }

    public Company getCompany() {
        return company;
    }

    public City getCity() {
        return city;
    }

    public Product getProduct() {
        return type.getProduct();
    }

    public int getInventory(Product product) {
        Integer amount = inventory.get(product);
        if (amount == null) {
            amount = 0;
        }
        return amount;
    }

    public void increaseInventory(Product product, int amount) {
        Integer currentAmount = inventory.get(product);
        if (currentAmount == null) {
            currentAmount = 0;
        }
        inventory.put(product, currentAmount + amount);
    }

    public void decreaseInventory(Product product, int amount) {
        Integer currentAmount = inventory.get(product);
        if (currentAmount == null) {
            currentAmount = 0;
        }
        currentAmount -= amount;
        if (currentAmount < 0) {
            String msg = String.format("Negative inventory (product: %s, count: %d", product, currentAmount);
            throw new IllegalStateException(msg);
        }
        inventory.put(product, currentAmount);
    }

    @Override
    public String toString() {
        return getName();
    }
}
