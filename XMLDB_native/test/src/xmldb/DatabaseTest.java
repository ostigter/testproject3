package xmldb;


import java.io.File;


/**
 * Test driver.
 * 
 * @author  Oscar Stigter
 */
public class DatabaseTest {
	
	
	private static final int NO_OF_DOCS = 10000;
	
//	private final Database database;
    

	public static void main(String[] args) {
	    new DatabaseTest();
	}
	
	
    public DatabaseTest() {
//    	database = Database.getInstance();
//        File dir = new File("C:/LocalData/Temp/results-01");
//        if (dir.exists() && dir.isDirectory()) {
//	        for (File file : dir.listFiles()) {
//	            if (file.isFile() && file.getName().endsWith(".xml")) {
//	                storeFile(file);
//	            }
//	        }
//        } else {
//        	System.err.println("Directory not found: " + dir);
//        }
        
        File file = new File("test/data/TestReport.xml");
        for (int i = 0; i < NO_OF_DOCS; i++) {
        	storeFile(file);
        }
        
        System.out.println(Util.getMemoryUsage());
    }
	
	
	private void storeFile(File file) {
        Document doc = new Document(file.getName());
        try {
            doc.setContent(file);
        } catch (XmldbException e) {
            System.err.println("ERROR: Could not store file '" + file + "': "
                    + e.getMessage());
        }
	}

}
