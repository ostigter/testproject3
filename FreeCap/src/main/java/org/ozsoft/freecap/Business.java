package org.ozsoft.freecap;

import static org.ozsoft.freecap.Utils.money;

import java.util.HashMap;
import java.util.Map;

public abstract class Business implements GameListener {

    protected final BusinessType type;

    protected final int id;

    protected final Company company;

    protected final City city;

    protected final Map<Product, Integer> stock;

    protected Business supplier;

    protected double salesPrice;

    public Business(BusinessType type, int id, Company company, City city) {
        this.type = type;
        this.id = id;
        this.company = company;
        this.city = city;
        stock = new HashMap<Product, Integer>();
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

    public double getBuildingCost() {
        return type.getBuildingCost();
    }

    public Business getSupplier() {
        return supplier;
    }

    public void setSupplier(Business supplier) {
        this.supplier = supplier;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
        System.out.format("%s sets sales price to %s.\n", this, money(salesPrice));
    }

    public int getStock(Product product) {
        Integer amount = stock.get(product);
        if (amount == null) {
            amount = 0;
        }
        return amount;
    }

    public void increaseStock(Product product, int amount) {
        Integer currentAmount = stock.get(product);
        if (currentAmount == null) {
            currentAmount = 0;
        }
        stock.put(product, currentAmount + amount);
    }

    public void decreaseStock(Product product, int amount) {
        Integer currentAmount = stock.get(product);
        if (currentAmount == null) {
            currentAmount = 0;
        }
        currentAmount -= amount;
        if (currentAmount < 0) {
            String msg = String.format("Negative stock (product: %s, count: %d)", product, currentAmount);
            throw new IllegalStateException(msg);
        }
        stock.put(product, currentAmount);
    }

    public int sellResource(Product product, int amount) {
        int amountInStock = getStock(product);
        int amountSold = (amountInStock >= amount) ? amount : amountInStock;
        decreaseStock(product, amountSold);
        return amountSold;
    }

    public void pay(Business recipient, double amount) {
        company.pay(amount);
        recipient.receivePayment(amount);
    }

    public void receivePayment(double amount) {
        company.receivePayment(amount);
    }

    @Override
    public String toString() {
        return getName();
    }

    protected void payWeeklyCosts() {
        double costs = type.getWeeklyCost();
        System.out.format("%s pays %s for weekly costs.\n", this, money(costs));
        company.pay(costs);
    }
}
