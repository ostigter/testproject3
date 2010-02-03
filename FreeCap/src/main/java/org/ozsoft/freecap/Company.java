package org.ozsoft.freecap;

import java.util.HashMap;
import java.util.Map;

public class Company implements GameListener {
    
    private final String name;
    
    private final Map<String, Enterprise> enterprises;
    
    public Company(String name) {
        this.name = name;
        enterprises = new HashMap<String, Enterprise>();
    }
    
    public String getName() {
        return name;
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
        for (Enterprise enterprise : enterprises.values()) {
            enterprise.doNextTurn();
        }
    }

}
