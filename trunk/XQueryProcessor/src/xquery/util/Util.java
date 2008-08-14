package xquery.util;


import java.io.File;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.DOMConfigurator;


/**
 * Static class with various utility methods.
 *  
 * @author Oscar Stigter
 */
public class Util {
    
    
    /** Config file for log4j. */
    private static final String LOG_CONFIG_FILE = "log4j.xml";
    
    /** Indicates whether log4j has been initialized. */
    private static boolean logInitialized = false; 
    
    
    /**
     * Private constructor to deny instantiation.
     */
    private Util() {
        // Empty implementation.
    }
    
    
    /**
     * Configures log4j.
     */
    public static void initLog4j() {
        if (!logInitialized) {
            File file = new File(LOG_CONFIG_FILE);
            if (file.isFile() && file.canRead()) {
                DOMConfigurator.configure(file.getPath());
            } else {
                Logger rootLogger = Logger.getRootLogger();
                rootLogger.removeAllAppenders();
                rootLogger.addAppender(new ConsoleAppender(
                        new PatternLayout("%d %-5p %F:%M:%L - %m%n")));
                rootLogger.setLevel(Level.INFO);
            }
            
            logInitialized = true;
        }
    }
    
    
    /**
     * Deletes a file or directory (recursively).
     * 
     * @param  path  the file path
     */
    public static void deleteFile(String path) {
        deleteFile(new File(path));
    }
    

    /**
     * Deletes a file or directory (recursively).
     * 
     * @param  file  the file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deleteFile(f);
                }
            }
            file.delete();
        }
    }
    
    
}
