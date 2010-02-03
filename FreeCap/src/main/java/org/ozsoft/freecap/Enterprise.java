package org.ozsoft.freecap;

import java.util.HashMap;
import java.util.Map;

public abstract class Enterprise implements GameListener {
    
    protected String name;
    
    protected Product product;
    
    protected final Company company;
    
    protected final City city;
    
    protected final Map<Product, InventoryItem> inventory;
    
    public Enterprise(String name, Product product, Company company, City city) {
        this.name = name;
        this.product = product;
        this.company = company;
        this.city = city;
        inventory = new HashMap<Product, InventoryItem>();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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
        return name;
    }

}
