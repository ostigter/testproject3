package org.ozsoft.fondsbeheer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Closing;
import org.ozsoft.fondsbeheer.entities.Fund;
import org.ozsoft.fondsbeheer.services.DatabaseException;
import org.ozsoft.fondsbeheer.services.FundService;
import org.ozsoft.fondsbeheer.services.FundServiceImpl;

public class Main {
    
    private static final Logger LOG = Logger.getLogger(Main.class);
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static void main(String[] args) throws Exception {
        LOG.info("Started");
        
        FundService fundService = new FundServiceImpl();
        
        try {
            Category category = new Category();
            category.setId("c1");
            category.setName("Category_1");
            fundService.storeCategory(category);
            category = new Category();
            category.setId("c2");
            category.setName("Category_2");
            fundService.storeCategory(category);
            category = new Category();
            category.setId("c3");
            category.setName("Category_3");
            fundService.storeCategory(category);
            
            for (Category c : fundService.findCategories()) {
                LOG.info(String.format("Category: '%s'", c.getName()));
            }
            
            category = fundService.findCategory("c2");
            LOG.info(String.format("Category: '%s'", category.getName()));
            
            Fund fund = new Fund();
            fund.setId("f1");
            fund.setName("Fund_1");
            fund.setCategoryId("c1");
            fundService.storeFund(fund);
            fund = new Fund();
            fund.setId("f2");
            fund.setName("Fund_2");
            fund.setCategoryId("c1");
            fundService.storeFund(fund);
            fund = new Fund();
            fund.setId("f3");
            fund.setName("Fund_3");
            fund.setCategoryId("c2");
            fundService.storeFund(fund);
            
            for (Fund f : fundService.findFunds()) {
                LOG.info(String.format("Fund: '%s'", f.getName()));
            }
            
            for (Fund f : fundService.findFunds("c1")) {
                LOG.info(String.format("Fund: '%s'", f.getName()));
            }
            
            fund = fundService.findFund("f3");
            LOG.info(String.format("Fund: '%s'", fund.getName()));
            
            Closing closing = new Closing();
            closing.setFundId("f1");
            closing.setDate(DATE_FORMAT.parse("20090130"));
            closing.setPrice(12.34);
            fundService.storeClosing(closing);
            
            fundService.deleteFund("f1");
            fundService.deleteFund("f2");
            fundService.deleteFund("f3");
            
            fundService.deleteCategory("c1");
            fundService.deleteCategory("c2");
            fundService.deleteCategory("c3");
            
        } catch (DatabaseException e) {
            LOG.error("Database error", e);
        }
        
        LOG.info("Finished");
    }
    
}
