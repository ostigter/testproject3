package org.ozsoft.freecap;

import java.util.HashMap;
import java.util.Map;

public class City implements GameListener {
    
    private final String name;
    
    private int population;
    
    private final Map<String, Enterprise> enterprises;
    
    public City(String name, int population) {
        this.name = name;
        this.population = population;
        enterprises = new HashMap<String, Enterprise>();
    }
    
    public String getName() {
        return name;
    }
    
    public int getPopulation() {
        return population;
    }
    
    public void addEnterprise(Enterprise enterprise) {
        enterprises.put(enterprise.getName(), enterprise);
    }
    
    public Enterprise getEnterprise(String name) {
        return enterprises.get(name);
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
