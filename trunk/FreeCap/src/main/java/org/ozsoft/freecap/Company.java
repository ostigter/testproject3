package org.ozsoft.freecap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Company implements GameListener {

    private final String name;

    private final Map<String, Business> businesses;

    private double cash;

    public Company(String name, double cash) {
        this.name = name;
        this.cash = cash;
        businesses = new HashMap<String, Business>();
    }

    public String getName() {
        return name;
    }

    public double getCash() {
        return cash;
    }

    public void pay(double amount) {
        if (amount > cash) {
            throw new IllegalArgumentException(String.format("%s has insufficient cash (%,.2f) to pay %,.2f!", this, cash, amount));
        }

        cash -= amount;
        System.out.format("%s payed $%,.2f (cash: $%,.0f)\n", this, amount, cash);
    }

    public void receivePayment(double amount) {
        cash += amount;
        System.out.format("%s received $%,.2f (cash: $%,.0f)\n", this, amount, cash);
    }

    public Collection<Business> getBusinesses() {
        return businesses.values();
    }

    public Business getBusiness(String name) {
        return businesses.get(name);
    }

    public void addBusiness(Business business) {
        businesses.put(business.getName(), business);
    }

    @Override
    public void doNextTurn() {
        // TODO: doNextTurn for a company.
    }

    @Override
    public String toString() {
        return name;
    }
}
