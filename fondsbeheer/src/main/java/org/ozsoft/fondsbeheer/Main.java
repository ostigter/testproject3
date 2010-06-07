package org.ozsoft.fondsbeheer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Fund;
import org.ozsoft.fondsbeheer.entities.FundValue;
import org.ozsoft.fondsbeheer.entities.SmallDate;
import org.ozsoft.fondsbeheer.services.FundService;
import org.ozsoft.fondsbeheer.services.FundServiceImpl;

public class Main {

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
//        LOG.info("Number of updated quotes: " + fundService.getNoOfUpdatedFunds());

//        Fund fund = category.getFund("aex-index");
//        fundService.retrieveFund(fund);
//        fundService.updateFund(fund);
//        for (FundValue value : fund.getValues()) {
//        	LOG.info("Value: " + value);
//        }
//        LOG.info("Number of values: " + fund.getNoOfValues());
        
//        Fund fund = new Fund("ahold", "Ahold");
//        fundService.retrieveFund(fund);
//        for (FundValue fundValue : fund.getValues()) {
//        	System.out.println(fundValue);
//        }
        
//        fundService.checkIntegrity();
        
//        fundService.printDiskUsage();
        
//        File importDir = new File("import");
//        if (!importDir.isDirectory()) {
//            importDir.mkdir();
//        }
//        for (File dir : importDir.listFiles()) {
//            if (dir.isDirectory()) {
//                String categoryId = dir.getName();
//                Category category = fundService.getCategory(categoryId);
//                if (category != null) {
//                    for (File file : dir.listFiles()) {
//                        if (file.isFile()) {
//                            String fundId = file.getName().replaceAll(".csv$", "");
//                            Fund fund = category.getFund(fundId);
//                            if (fund != null) {
//                                System.out.format("%s / %s\t\t('%s')\n", category, fund, fundId);
//                            	try {
//                            		fund.clear();
//                            		BufferedReader br = new BufferedReader(new FileReader(file));
//                            		String line = null;
//                            		while ((line = br.readLine()) != null) {
//                            			String[] tokens = line.split(";");
//                            			try {
//                            				SmallDate date = SmallDate.parseDate(tokens[0]);
//                            				float value = Float.parseFloat(tokens[1]);
//                            				fund.addValue(new FundValue(date, value));
//                            			} catch (Exception e) {
//                            				System.err.println(e);
//                            			}
//                            		}
//                            		br.close();
//                            		fundService.storeFund(fund);
//                            		fund.clear();
//                            	} catch (IOException e) {
//                            		System.err.println(e);
//                            	}
//                            } else {
//                                System.err.format("*** ERROR: Fund not found with ID '%s'\n", fundId);
//                            }
//                        }
//                    }
//                } else {
//                    System.err.format("*** ERROR: Category not found with ID '%s'\n", categoryId);
//                }
//            }
//        }
        
        Fund fund = new Fund("f1", "F2");
        fund.addValue(new FundValue(new SmallDate(1, 1, 2010), 1.0f));
        fundService.storeFund(fund);
        fund = new Fund("f2", "F2");
        fund.addValue(new FundValue(new SmallDate(2, 1, 2010), 2.0f));
        fundService.storeFund(fund);
        
		fundService.retrieveFund(fund);
		for (FundValue fundValue : fund.getValues()) {
			System.out.println(fundValue);
		}        
        
        fundService.stop();
    }
    
}
