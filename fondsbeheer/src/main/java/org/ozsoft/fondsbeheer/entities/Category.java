package org.ozsoft.fondsbeheer.entities;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/**
 * A fund category.
 * 
 * @author Oscar Stigter
 */
public class Category {
    
    private final String id;
    
    private final String name;
    
    private final Map<String, Fund> funds;
    
    public Category(String id, String name) {
        this.id = id;
        this.name = name;
        funds = new TreeMap<String, Fund>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addFund(Fund fund) {
        funds.put(fund.getId(), fund);
    }
    
    public int getNoOfFunds() {
        return funds.size();
    }

    public Collection<Fund> getFunds() {
        return funds.values();
    }
    
    public Fund getFund(String id) {
        return funds.get(id);
    }
    
    public void removeFund(String id) {
        funds.remove(id);
    }
    
    @Override
    public int hashCode() {
        return id.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Category) {
            return id.equals(((Category) obj).getId());
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return name; 
    }
    
}
