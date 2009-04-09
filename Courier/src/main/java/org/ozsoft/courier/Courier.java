package org.ozsoft.courier;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A lightweight XML message router.
 * 
 * @author Oscar Stigter
 */
public class Courier {
	
	/** The log. */
	private static final Logger LOG = Logger.getLogger(Courier.class);
    
    /** The handlers. */
    private final Set<Handler> handlers;
    
    /**
     * Constructor.
     */
    public Courier() {
        LOG.debug("Creating");
        
        handlers = new HashSet<Handler>();
        
        parseConfigFile();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOG.debug("Shutting down...");
                shutdown();
            }
        });
        
        LOG.debug("Created");
    }
    
    /**
     * The application's entry point.
     * 
     * @param args
     *            Command line arguments.
     */
    public static void main(String[] args) {
        new Courier().start();
    }
    
    /**
     * Starts the application.
     */
    public void start() {
        for (Handler handler : handlers) {
            handler.start();
        }
        LOG.info("Started");
    }
    
    /**
     * Shuts down the application.
     */
    public void shutdown() {
        for (Handler handler : handlers) {
            handler.stop();
        }
        LOG.info("Shut down");
    }
    
    /**
     * Reads the configuration file.
     */
    private void parseConfigFile() {
        try {
            // Create a File handler.
            Handler handler = new FileHandler("data/in", 1000L);
    
            // Add a Log action.
            handler.addAction(new LogAction());
            
            // Add a XSLT transformation action. 
            handler.addAction(new XsltAction("resources/request.xsl"));
    
            // Add another Log action.
            handler.addAction(new LogAction());
            
            // Add a FileOut action.
            FileOutAction fileOutAction = new FileOutAction(
                    "data/hist", "concat('request-', //res:TicketNr/text(), '.xml')");
            fileOutAction.addNamespace("res", "http://www.example.com/response");
            handler.addAction(fileOutAction);
            
            handlers.add(handler);
            
        } catch (CourierException e) {
            LOG.fatal("Error parsing configuration file", e);
            System.exit(1);
        }
    }
    
}
