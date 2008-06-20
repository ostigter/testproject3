package streamvalidator;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;


/**
 * Test driver for the StreamValidator class.
 * 
 * @author Oscar Stigter
 */
public class StreamValidatorTest {
    
    
    private static final String SCHEMA_ROOT = "schemas";
    
    private static final String INPUT_DOCUMENT  = "documents/document.xml";

    private static final String OUTPUT_DOCUMENT = "documents/out.xml";


    public static void main(String[] args) {
        long startTime;
        long duration;

        startTime = System.currentTimeMillis();
        StreamValidator validator = new StreamValidator();
        duration = System.currentTimeMillis() - startTime;
        System.out.println("Validator created in " + duration + " ms.");
        printMemoryUsage();

        validator.setSchemaRoot(SCHEMA_ROOT);
        printMemoryUsage();

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(INPUT_DOCUMENT);
            os = new FileOutputStream(OUTPUT_DOCUMENT);

            startTime = System.currentTimeMillis();
            validator.validate(is, os);
            duration = System.currentTimeMillis() - startTime;
            System.out.println("Document validated in " + duration + " ms.");
            printMemoryUsage();
        } catch (IOException e) {
            throw new RuntimeException("I/O error: " + e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Ignore.
                }
            }
        }
    }


    public static void printMemoryUsage() {
        long totalBytes = Runtime.getRuntime().totalMemory();
        long freeBytes = Runtime.getRuntime().freeMemory();
        double usedMB = (totalBytes - freeBytes) / 1048576.0;
        System.out.println(String.format(
                Locale.US, "Used memory: %.2f MB", usedMB));
    }

}
