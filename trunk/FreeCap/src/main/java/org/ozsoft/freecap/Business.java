package org.ozsoft.freecap;

import java.util.HashMap;
import java.util.Map;

public abstract class Business implements GameListener {

    protected final BusinessType type;

    protected final int id;

    protected final Product product;

    protected final Company company;

    protected final City city;

    protected final Map<Product, InventoryItem> inventory;

    public Business(BusinessType type, int id, Product product, Company company, City city) {
        this.type = type;
        this.id = id;
        this.product = product;
        this.company = company;
        this.city = city;
        inventory = new HashMap<Product, InventoryItem>();
    }

    public String getName() {
        return String.format("%s '%s-%02d'", type.getName(), type.getPrefix(), id);
    }

    public Product getProduct() {
        return product;
    }

    public Company getCompany() {
        return company;
    }

    public City getCity() {
        return city;
    }

    public InventoryItem getInventoryItem(Product product) {
        InventoryItem item = inventory.get(product);
        if (item == null) {
            item = new InventoryItem(product);
            inventory.put(product, item);
        }
        return item;
    }

    @Override
    public String toString() {
        return getName();
    }
}
