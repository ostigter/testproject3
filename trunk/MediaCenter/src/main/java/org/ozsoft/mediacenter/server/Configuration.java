package org.ozsoft.mediacenter.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.ozsoft.mediacenter.shared.Constants;

/**
 * The server configuration.
 *  
 * @author Oscar Stigter
 */
public class Configuration {
	
	/** The logger. */
	private static final Logger LOG = Logger.getLogger(Configuration.class);
    
	/** The server port. */
    private int serverPort = Constants.DEFAULT_SERVER_PORT;
    
    /** The TV show root directories. */
    private final List<String> showRoots;
    
    /**
     * Constructor.
     */
    public Configuration() {
        showRoots = new ArrayList<String>();
        readConfigFile();
    }
    
    /**
	 * Returns the server port.
	 * 
	 * @return The server port.
	 */
    public int getServerPort() {
    	return serverPort;
    }
    
    /**
	 * Returns the TV show root directories.
	 * 
	 * @return The TV show root directories.
	 */
    public List<String> getShowRoots() {
    	return showRoots;
    }
    
    /**
     * Reads the configuration file.
     */
    private void readConfigFile() {
        File file = new File(Constants.INI_FILE);
        if (!file.exists()) {
        	String msg = String.format(
        			"ERROR: Configuration file '%s' not found",
        			Constants.INI_FILE);
        	LOG.fatal(msg);
            throw new RuntimeException(msg);
        }
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                int pos = line.indexOf('=');
                if (pos != -1) {
                    String key = line.substring(0, pos).trim();
                    String value = line.substring(pos + 1).trim();
                    if (key.equals("server.port")) {
                    	try {
                    		serverPort = Integer.parseInt(value);
                    		LOG.info(String.format(""));
                    	} catch (NumberFormatException e) {
                    		String msg = String.format(
                    				"Error in configuration file: Could not parse server port: '%s'",
                    				value);
                    		LOG.fatal(msg);
                    		throw new RuntimeException(msg);
                    	}
                    } else if (key.equals("shows.root")) {
                    	if (new File(value).isDirectory()) {
                            showRoots.add(value);
                    	} else {
                    		String msg = String.format(
                    				"Error in configuration file: Invalid root directory: '%s'",
                    				value);
                    		LOG.fatal(msg);
                    		throw new RuntimeException(msg);
                    	}
                    } else {
                        // Unparsable line; ignore.
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println(
                    "ERROR: Could not read configuration file: " + e);
        }
    }
    
}
