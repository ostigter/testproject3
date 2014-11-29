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
        System.out.println("\ndoNextTurn for city " + name);

        // Produce raw products.
        for (Business business : businesses.values()) {
            Product product = business.getProduct();
            if (product.getLevel() == ProductLevel.RAW) {
                System.out.println("Raw product: " + product);
            }
        }

        // Produce semi products.
        for (Business business : businesses.values()) {
            Product product = business.getProduct();
            if (product.getLevel() == ProductLevel.INTERMEDIATE) {
                System.out.println("Intermediate product: " + product);
            }
        }

        // Produce consumer products and consume them.
        for (Business business : businesses.values()) {
            Product product = business.getProduct();
            if (product.getLevel() == ProductLevel.FINISHED) {
                int demand = population * product.getBaseDemand();
                System.out.format("Finished product: %s (demand: %d)\n", product, demand);
            }
        }
    }
}
