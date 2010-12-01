package org.ozsoft.freecap;

import java.util.HashMap;
import java.util.Map;

public class City implements GameListener {
    
    private final String name;
    
    private int population;
    
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
    
    public void addEnterprise(Business enterprise) {
        businesses.put(enterprise.getName(), enterprise);
    }
    
    public Business getEnterprise(String name) {
        return businesses.get(name);
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public void doNextTurn() {
        System.out.println(name + ": Processing next turn");
    }

}
