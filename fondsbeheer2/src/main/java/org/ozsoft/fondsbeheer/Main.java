package org.ozsoft.fondsbeheer;

import org.apache.log4j.Logger;
import org.ozsoft.fondsbeheer.entities.Fund;
import org.ozsoft.fondsbeheer.services.DatabaseException;
import org.ozsoft.fondsbeheer.services.FundService;
import org.ozsoft.fondsbeheer.services.JdbcFundService;

public class Main {
    
    private static final Logger LOG = Logger.getLogger(Main.class);
    
//    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static void main(String[] args) throws Exception {
        LOG.info("Started");
        
        try {
            FundService fundService = new JdbcFundService();
            fundService.connect();

//            List<Category> categories = fundService.getCategories();
//            for (Category c : categories) {
//                LOG.info("Category: " + c);
//            }
            
//            Category category = new Category();
//            category.setId("c1");
//            category.setName("Category_1");
//            fundService.storeCategory(category);
//            category = new Category();
//            category.setId("c2");
//            category.setName("Category_2");
//            fundService.storeCategory(category);
//            category = new Category();
//            category.setId("c3");
//            category.setName("Category_3");
//            fundService.storeCategory(category);
//            for (Category c : fundService.getCategories()) {
//                LOG.info(String.format("Category: '%s'", c.getName()));
//            }
//            category = fundService.getCategory("c2");
//            LOG.info(String.format("Category: '%s'", category.getName()));
//            
//            Fund fund = new Fund();
//            fund.setId("f1");
//            fund.setName("Fund_1");
//            fund.setCategoryId("c1");
//            fundService.storeFund(fund);
//            fund = new Fund();
//            fund.setId("f2");
//            fund.setName("Fund_2");
//            fund.setCategoryId("c1");
//            fundService.storeFund(fund);
//            fund = new Fund();
//            fund.setId("f3");
//            fund.setName("Fund_3");
//            fund.setCategoryId("c2");
//            fundService.storeFund(fund);
//            for (Fund f : fundService.getFunds()) {
//                LOG.info(String.format("Fund: '%s'", f.getName()));
//            }
            for (Fund f : fundService.getFunds("OSLO")) {
                LOG.info(String.format("Fund: '%s'", f.getName()));
            }
//            fund = fundService.getFund("f3");
//            LOG.info(String.format("Fund: '%s'", fund.getName()));
//            
//            Closing closing = new Closing();
//            closing.setFundId("f1");
//            closing.setDate(DATE_FORMAT.parse("20090130"));
//            closing.setPrice(10.00);
//            fundService.storeClosing(closing);
//            closing = new Closing();
//            closing.setFundId("f1");
//            closing.setDate(DATE_FORMAT.parse("20090131"));
//            closing.setPrice(10.25);
//            fundService.storeClosing(closing);
//            closing = new Closing();
//            closing.setFundId("f1");
//            closing.setDate(DATE_FORMAT.parse("20090201"));
//            closing.setPrice(10.50);
//            fundService.storeClosing(closing);
            
            fundService.close();
            
        } catch (DatabaseException e) {
            LOG.error("Database error", e);
        }
        
        LOG.info("Finished");
    }
    
}
