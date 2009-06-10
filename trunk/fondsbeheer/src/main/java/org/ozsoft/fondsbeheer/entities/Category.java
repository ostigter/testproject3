package org.ozsoft.fondsbeheer.entities;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * A fund category.
 * 
 * @author Oscar Stigter
 */
public class Category {
    
	/** The logger. */
	private static final Logger LOG = Logger.getLogger(Category.class);
	
	/** The ID. */
	private final String id;
    
    /** The name. */
	private final String name;
    
    /** The funds mapped by ID. */
	private final Map<String, Fund> funds;
    
    /**
	 * Constructor.
	 * 
	 * @param id
	 *            The ID.
	 * @param name
	 *            The name.
	 */
	public Category(String id, String name) {
        this.id = id;
        this.name = name;
        funds = new TreeMap<String, Fund>();
    }

	/**
	 * Returns the ID.
	 * 
	 * @return The ID.
	 */
	public String getId() {
        return id;
    }

	/**
	 * Returns the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
        return name;
    }

	/**
	 * Adds a fund.
	 * 
	 * @param fund
	 *            The fund.
	 */
	public boolean addFund(Fund fund) {
		boolean added = false;
		String id = fund.getId();
		if (!funds.containsKey(id)) {
			funds.put(fund.getId(), fund);
			added = true;
			LOG.info(String.format("Added fund '%s' to category '%s'", fund, this));
		}
		return added;
    }
    
	/**
	 * Returns the number of funds.
	 * 
	 * @return The number of funds.
	 */
	public int getNoOfFunds() {
        return funds.size();
    }

	/**
	 * Returns the funds.
	 * 
	 * @return The funds.
	 */
	public Collection<Fund> getFunds() {
        return funds.values();
    }
    
	/**
	 * Returns a fund specified by ID.
	 * 
	 * @param id
	 *            The fund ID.
	 * 
	 * @return The fund if found, otherwise null.
	 */
	public Fund getFund(String id) {
        return funds.get(id);
    }
    
	/**
	 * Removes a fund.
	 * 
	 * @param id The fund ID.
	 */
	public boolean removeFund(String id) {
		boolean removed = false;
		if (funds.containsKey(id)) {
	        funds.remove(id);
	        removed = true;
		}
		return removed;
    }
    
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
    public int hashCode() {
        return id.hashCode();
    }
    
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object obj) {
        if (obj instanceof Category) {
            return id.equals(((Category) obj).getId());
        } else {
            return false;
        }
    }
    
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
    public String toString() {
        return name; 
    }
    
}
