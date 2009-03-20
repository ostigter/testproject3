package org.ozsoft.mediacenter.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.ozsoft.mediacenter.shared.Constants;

/**
 * The client configuration.
 * 
 * @author Oscar Stigter
 */
public class Configuration {
	
	/** The logger. */
	private static final Logger LOG = Logger.getLogger(Configuration.class);
    
	/** The server host. */
	private String serverHost = Constants.DEFAULT_SERVER_HOST;
    
	/** The server port. */
    private int serverPort = Constants.DEFAULT_SERVER_PORT;
    
	/** Path of the external application to play video media. */
    private String videoPlayerPath;
    
    /**
     * Constructor.
     */
    public Configuration() {
        readConfigFile();
    }
    
    /**
	 * Returns the server host.
	 * 
	 * @return The server host.
	 */
    public String getServerHost() {
    	return serverHost;
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
	 * Returns the path of the application to play video media.
	 * 
	 * @return The path of the application to play video media.
	 */
    public String getVideoPlayerPath() {
        return videoPlayerPath;
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
                    if (key.equals("server.host")) {
                        serverHost = value;
                    } else if (key.equals("server.port")) {
                    	try {
                    		serverPort = Integer.parseInt(value);
                    	} catch (NumberFormatException e) {
                    		String msg = String.format(
                    				"Error in configuration file: Could not parse server port: '%s'",
                    				value);
                    		LOG.fatal(msg);
                    		throw new RuntimeException(msg);
                    	}
                    } else if (key.equals("video.player")) {
                    	if (new File(value).isFile()) {
                            videoPlayerPath = value;
                    	} else {
                    		String msg = String.format(
                    				"Error in configuration file: Video player executable not found: '%s'",
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
