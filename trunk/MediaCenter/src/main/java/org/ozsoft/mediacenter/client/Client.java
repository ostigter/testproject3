package org.ozsoft.mediacenter.client;

import org.apache.log4j.Logger;
import org.ozsoft.genie.GenieClient;
import org.ozsoft.genie.GenieException;
import org.ozsoft.mediacenter.domain.Show;
import org.ozsoft.mediacenter.shared.Constants;

/**
 * The client.
 * 
 * @author Oscar Stigter
 */
public class Client {
	
	/** The log. */
	private static final Logger LOG = Logger.getLogger(Client.class);
	
	/** The confguration. */
	private final Configuration config;
	
    /**
     * Program's main entry point.
     * 
     * @param args  Command line arguments.
     */
    public static void main(String[] args) {
    	new Client();
    }
    
    /**
     * Constructor.
     */
    public Client() {
    	config = new Configuration();

    	LOG.info("Started");
    	
    	try {
	    	LOG.debug("Connecting");
	    	String host = config.getServerHost();
	    	int port = config.getServerPort();
			GenieClient client =
					new GenieClient(host, port, Constants.SERVICE_ID);
			
	    	LOG.info("Retrieving TV shows");
			Show[] shows = (Show[]) client.call("getShows");
			for (Show show : shows) {
				System.out.println(show);
			}
			
	    	LOG.info("Finished");
	    	
		} catch (GenieException e) {
    		System.err.println("ERROR: " + e);
    	}
    }

}
