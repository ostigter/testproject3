package org.ozsoft.fondsbeheer;

import org.apache.log4j.Logger;
import org.ozsoft.fondsbeheer.services.FundService;
import org.ozsoft.fondsbeheer.services.FundServiceImpl;

public class Main {

	/** The logger. */
	private static final Logger LOG = Logger.getLogger(Main.class);
	
    public static void main(String[] args) {
//    	new MainForm();
        run();
    }
    
    private static void run() {
        FundService fundService = new FundServiceImpl();
        fundService.start();
        LOG.info("Number of categories:     " + fundService.getNoOfCategories());
        LOG.info("Number of quotes:         " + fundService.getNoOfFunds());
        
//        fundService.updateCategories();
//        LOG.info("Number of new categories: " + fundService.getNoOfNewCategories());
//        LOG.info("Number of new quotes:     " + fundService.getNoOfNewFunds());
//        LOG.info("Number of updated quotes: " + fundService.getNoOfUpdatedFunds());
        
//        fundService.resetUpdateCounter();
//        fundService.updateAll();
//        Category category = fundService.getCategory("INDEXEN");
//        Category category = fundService.getCategory("HOOFDFONDSEN");
//        Category category = fundService.getCategory("BELEGGINGSFUNDS");
//        Category category = fundService.getCategory("USA");
//        fundService.updateFundsInCategory(category);
//        Fund fund = category.getFund("aalberts");
//        fundService.updateFund(fund);
//        for (FundValue value : fund.getValues()) {
//        	LOG.debug("Value: " + value);
//        }
//        LOG.info("Number of values: " + fund.getNoOfValues());
//        LOG.info("Number of updated quotes: " + fundService.getNoOfUpdatedFunds());
        
//        fundService.checkIntegrity();
        
        fundService.printDiskUsage();
        
        fundService.stop();
    }
    
}
