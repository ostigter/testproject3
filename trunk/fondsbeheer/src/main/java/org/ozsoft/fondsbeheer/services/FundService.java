package org.ozsoft.fondsbeheer.services;

import java.util.Collection;

import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Fund;

/**
 * Fund service.
 * 
 * Handles the retrieving and storing of funds and categories, and updating
 * them from the internet.
 * 
 * @author Oscar Stigter
 */
public interface FundService {
	
	/**
	 * Sets the data directory.
	 * 
	 * @param path
	 *            The path to the data directory.
	 */
	void setDataDirectory(String path);
	
	/**
	 * Returns the data directory.
	 * 
	 * @return The data directory.
	 */
	String getDataDirectory();
	
	/**
	 * Starts the service.
	 */
	void start();
	
	/**
	 * Stops the service.
	 * 
	 * Clients MUST call this method before exiting the JVM to guarantee that
	 * changes are flushed to disk.  
	 */
	void stop();
	
	/**
	 * Returns the number of categories.
	 * 
	 * @return The number of categories.
	 */
	int getNoOfCategories();
	
	/**
	 * Returns the categories.
	 * 
	 * @return The categories.
	 */
	Collection<Category> getCategories();
	
	/**
	 * Returns a category specified by its ID.
	 * 
	 * @param categoryId
	 *            The category ID.
	 * 
	 * @return The category if found, otherwise false.
	 */
	Category getCategory(String categoryId);
	
	/**
	 * Adds a new category.
	 * 
	 * @param category The category.
	 */
	void addCategory(Category category);

	/**
	 * Returns the total number of funds.
	 * 
	 * @return The total number of funds.
	 */
	int getNoOfFunds();
	
	/**
	 * Returns a fund specified by its ID.
	 * 
	 * @param fundId
	 *            The fund ID.
	 * 
	 * @return The fund if found, otherwise null.
	 */
	void retrieveFund(Fund fund);
	
	/**
	 * Stores a fund.
	 * 
	 * @param fund
	 *            The fund.
	 */
	void storeFund(Fund fund);
	
	/**
	 * Updates the categories, looking for new categories and funds.
	 * 
	 * This method does not update any funds.
	 */
	void updateCategories();
	
	/**
	 * Updates all funds in all categories.
	 * 
	 * WARNING: This may take a long time to finish!
	 */
	void updateAll();
	
	/**
	 * Updates all funds in a specific category.
	 * 
	 * @param category
	 *            The category.
	 */
	void updateFundsInCategory(Category category);
	
	/**
	 * Updates a fund.
	 * 
	 * @param fund
	 *            The fund.
	 */
	void updateFund(Fund fund);
	
	int getNoOfNewCategories();
	
	int getNoOfNewFunds();
	
	int getNoOfUpdatedFunds();
	
	void resetUpdateCounter();
	
	void printDiskUsage();
	
}
