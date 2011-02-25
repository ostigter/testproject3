package xmlvalidator;

import java.io.File;

/**
 * Application's main class.
 *
 * @author  Oscar Stigter
 */
public class Main {
    
    private final XmlValidator validator = new XmlValidator();
    
    private String schemaRoot = ".";
    
    private String documentRoot = ".";
    
    private int validated = 0;
    
    private int errors = 0;
    
    private int warnings = 0;
    
    /**
     * The application's entry point.
     * 
     * @param  args  The command line arguments.
     */
    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        validator.setSchemaRootDir(schemaRoot);
        validateXmlFiles(documentRoot);
        System.out.format("Processed %d documents, %d errors, %d warnings.\n", validated, errors, warnings);
    }
    
    private void validateXmlFiles(String path) {
        validateXmlFiles(new File(path));
    }

    /**
     * Validates all XML files in the specified directory and below.
     * 
     * @param  dir  the directory to process
     */
    private void validateXmlFiles(File dir) {
        // First process files in this directory...
        for (File file : dir.listFiles()) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".xml")) {
                try {
                    validated++;
                    validator.validate(file);
                } catch (Exception e) {
                    System.out.println(String.format(
                            "ERROR: %s: %s", file, e.getMessage()));
                    errors++;
                }
            }
        }
        
        // ...then recurse into subdirectories.
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                validateXmlFiles(file);
            }
        }
    }
    
}
