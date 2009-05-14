package org.ozsoft.fondsbeheer.services;

import java.util.List;

import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Closing;
import org.ozsoft.fondsbeheer.entities.Fund;

public interface FundService {
    
    List<Category> findCategories();
    
    Category findCategory(String id);
    
    void storeCategory(Category category);
    
    void deleteCategory(String id);
    
    List<Fund> findFunds();
    
    List<Fund> findFundsByCategory(String categoryId);
    
    Fund findFund(String id);
    
    void storeFund(Fund fund);
    
    List<Closing> findClosings();
    
    List<Closing> findClosingsByFund(String fundId);
    
    void storeClosing(Closing closing);

}
