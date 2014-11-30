package org.ozsoft.freecap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Company implements GameListener {

    private final String name;

    private final Map<String, Business> businesses;

    public Company(String name) {
        this.name = name;
        businesses = new HashMap<String, Business>();
    }

    public String getName() {
        return name;
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
