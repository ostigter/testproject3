package org.ozsoft.freecap;

import java.util.HashMap;
import java.util.Map;

public class City implements GameListener {

    private final String name;

    private final int population;

    private final Map<String, Business> businesses;

    public City(String name, int population) {
        this.name = name;
        this.population = population;
        businesses = new HashMap<String, Business>();
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public void addBusiness(Business business) {
        businesses.put(business.getName(), business);
    }

    public Business getBusiness(String name) {
        return businesses.get(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void doNextTurn() {
        // TODO: doNextTurn() for a city.
    }
}
