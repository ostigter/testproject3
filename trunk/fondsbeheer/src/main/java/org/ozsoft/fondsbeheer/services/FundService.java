package org.ozsoft.fondsbeheer.services;

import java.util.List;

import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Closing;
import org.ozsoft.fondsbeheer.entities.Fund;

/**
 * Interface of the Fund service.
 * 
 * @author Oscar Stigter
 */
public interface FundService {
    
    void connect() throws DatabaseException;
    
    void close() throws DatabaseException;
    
    List<Category> getCategories() throws DatabaseException;
    
    Category getCategory(String categoryId) throws DatabaseException;
    
    void storeCategory(Category category) throws DatabaseException;
    
    List<Fund> getFunds() throws DatabaseException;
    
    List<Fund> getFunds(String categoryId) throws DatabaseException;
    
    Fund getFund(String fundId) throws DatabaseException;
    
    void storeFund(Fund fund) throws DatabaseException;
    
    List<Closing> getClosings(String fundId) throws DatabaseException;
    
    void storeClosing(Closing closing) throws DatabaseException;

}
