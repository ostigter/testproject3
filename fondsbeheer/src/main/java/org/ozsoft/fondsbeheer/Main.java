package org.ozsoft.fondsbeheer;

public class Main {

    public static void main(String[] args) {
        new Main().run();
    }
    
    private void run() {
        FundService fundService = new FundService();
        fundService.start();
        System.out.println("Number of categories:     " + fundService.getNoOfCategories());
        System.out.println("Number of quotes:         " + fundService.getNoOfFunds());
        
//        fundService.updateCategories();
//        System.out.println("Number of new categories: " + fundService.getNoOfNewCategories());
//        System.out.println("Number of new quotes:     " + fundService.getNoOfNewQuotes());
//        System.out.println("Number of updated quotes: " + fundService.getNoOfUpdatedQuotes());
        
//        fundService.resetUpdateCounter();
//        fundService.updateAll();
//        Category cat = fundService.getCategory("HOOFDFONDSEN");
//        Category cat = fundService.getCategory("BELEGGINGSFUNDS");
//        Category cat = fundService.getCategory("INDEXEN");
//        Category cat = fundService.getCategory("USA");
//        fundService.updateQuotesInCategory(cat);
//        System.out.println("Number of updated quotes: " + fundService.getNoOfUpdatedQuotes());
        
//        fundService.checkIntegrity();
        
//        fundService.printDiskUsage();
        
        fundService.shutdown();
        
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
