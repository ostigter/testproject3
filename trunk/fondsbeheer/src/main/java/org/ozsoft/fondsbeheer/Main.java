package org.ozsoft.fondsbeheer;

import org.ozsoft.fondsbeheer.services.FundService;
import org.ozsoft.fondsbeheer.services.FundServiceImpl;

public class Main {

    public static void main(String[] args) {
        new Main().run();
//    	new MainForm();
    }
    
    private void run() {
        FundService fundService = new FundServiceImpl();
        fundService.start();
        System.out.println("Number of categories:     " + fundService.getNoOfCategories());
        System.out.println("Number of quotes:         " + fundService.getNoOfFunds());
        
//        fundService.updateCategories();
//        System.out.println("Number of new categories: " + fundService.getNoOfNewCategories());
//        System.out.println("Number of new quotes:     " + fundService.getNoOfNewFunds());
//        System.out.println("Number of updated quotes: " + fundService.getNoOfUpdatedFunds());
        
//        fundService.resetUpdateCounter();
//        fundService.updateAll();
//        Category cat = fundService.getCategory("HOOFDFONDSEN");
//        Category cat = fundService.getCategory("BELEGGINGSFUNDS");
//        Category cat = fundService.getCategory("INDEXEN");
//        Category cat = fundService.getCategory("USA");
//        fundService.updateFundsInCategory(cat);
//        System.out.println("Number of updated quotes: " + fundService.getNoOfUpdatedFunds());
        
//        fundService.checkIntegrity();
        
        fundService.printDiskUsage();
        
        fundService.stop();
        
//        FileStore fileStore = new FileStore();
//        try {
//            fileStore.start();
//            byte[] bytes = fileStore.retrieve("asmlitho");
//            Quote quote = new Quote(bytes);
//            System.out.println("Fund: " + quote.getName());
//            System.out.println("Number of closings: " + quote.getNoOfClosings());
//            for (Closing closing : quote.getClosings()) {
//                System.out.println(closing);
//            }
//            fileStore.shutdown();
//        } catch (FileStoreException e) {
//            System.err.println(e);
//        }
    }
    
}
