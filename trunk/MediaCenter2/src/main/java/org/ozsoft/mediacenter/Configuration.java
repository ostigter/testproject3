package org.ozsoft.mediacenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The server configuration.
 *  
 * @author Oscar Stigter
 */
public class Configuration {
	
    /** The TV show root directories. */
    private final List<String> showRoots;
    
    /** The path to the media player application. */
    private String playerPath;
    
    /**
     * Constructor.
     */
    public Configuration() {
        showRoots = new ArrayList<String>();
        readConfigFile();
    }
    
    /**
     * Returns the path to the media player.
     * 
     * @return The path to the media player.
     */
    public String getPlayerPath() {
        return playerPath;
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
                    if (key.equals("player.path")) {
                        if (new File(value).isFile()) {
                            playerPath = value;
                        } else {
                            String msg = String.format("Error in configuration file: Media player executable not found: '%s'", value);
                            throw new RuntimeException(msg);
                        }
                        
                    } else if (key.equals("shows.root")) {
                    	if (new File(value).isDirectory()) {
                            showRoots.add(value);
                    	} else {
                    		String msg = String.format("Error in configuration file: Invalid root directory: '%s'", value);
                    		throw new RuntimeException(msg);
                    	}
                    } else {
                        // Unparsable line; ignore.
                        System.err.println("Warning: Unpasable line in configuration file: " + line);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("Could not read configuration file", e);
        }
    }
    
}
