package org.ozsoft.mediacenter.server;

import org.apache.log4j.Logger;
import org.ozsoft.genie.GenieException;
import org.ozsoft.genie.GenieServer;
import org.ozsoft.mediacenter.domain.Show;
import org.ozsoft.mediacenter.shared.Constants;

/**
 * The MediaCenter server.
 * 
 * @author Oscar Stigter
 */
public class Server {

    /** The logger. */
    private static final Logger LOG = Logger.getLogger(Server.class);
    
    /** The configuration. */
    private final Configuration config;
    
    /** The media library. */
    private final Library library;
    
    /** The Genie server. */
    private final GenieServer server;
    
	/**
	 * Program's entry point.
	 * 
	 * @param args  Command line arguments.
	 */
	public static void main(String[] args) {
		new Server();
	}
	
	/**
	 * Constructor.
	 */
	public Server() {
		LOG.debug("Starting");
		
		config = new Configuration();
		library = new Library(config);
		
		// Create Genie server and bind service.
		try {
			server = new GenieServer(config.getServerPort());
			server.bind(Constants.SERVICE_ID, this);
			server.start();
		} catch (GenieException e) {
			String msg = "Could not publish the server as Genie service";
			LOG.fatal(msg, e);
			throw new RuntimeException(msg, e);
		}
		
		LOG.info("Started");
	}
	
	public void refresh() {
    	LOG.debug("Operation 'refresh' called");
		library.refresh();
	}

    /**
     * Returns the TV shows.
     * 
     * @return The TV shows.
     */
    public Show[] getShows() {
    	LOG.debug("Operation 'getShows' called");
		return library.getShows();
	}

}
