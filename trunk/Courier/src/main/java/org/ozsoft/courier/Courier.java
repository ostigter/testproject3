package org.ozsoft.courier;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.ozsoft.courier.action.FileOut;
import org.ozsoft.courier.action.LogAction;
import org.ozsoft.courier.action.PropertySetter;
import org.ozsoft.courier.action.XsltTransformer;
import org.ozsoft.courier.connector.Connector;
import org.ozsoft.courier.connector.FilePoller;

/**
 * A lightweight XML message router.
 * 
 * @author Oscar Stigter
 */
public class Courier {
	
	/** The log. */
	private static final Logger LOG = Logger.getLogger(Courier.class);
    
    /** The namespace resolver. */
    private final NamespaceResolver namespaceResolver;
    
    /** The handlers. */
    private final Set<Connector> handlers;
    
    /**
     * Constructor.
     */
    public Courier() {
        LOG.debug("Starting");
        
        namespaceResolver = new NamespaceResolver();
        handlers = new HashSet<Connector>();
        
        parseConfigFile();

        for (Connector handler : handlers) {
            handler.start();
        }
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                shutdown();
            }
        });
        
        LOG.info("Started");
    }
    
	/**
	 * Application's entry point.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
        new Courier();
	}

    /**
     * Shuts down the application.
     */
    public void shutdown() {
        LOG.debug("Shutting down");
        for (Connector handler : handlers) {
            handler.stop();
        }
        LOG.info("Shut down");
    }
    
    /**
     * Reads the configuration file.
     */
    private void parseConfigFile() {
    	File file = new File("config.xml");
    	if (!file.isFile()) {
    		LOG.fatal("Configuration file not found");
    		System.exit(1);
    	} else {
	        try {
	        	// Namespace mappings.
	        	namespaceResolver.addNamespaceMapping("req", "http://www.example.com/request");
	        	namespaceResolver.addNamespaceMapping("res", "http://www.example.com/response");
	        	
	            // Create a File handler.
	            Connector handler = new FilePoller("data/in", 1000L, namespaceResolver);
	    
	            // Add a Log action.
	            handler.addAction(new LogAction(Level.INFO));
	            
	            // Set a variable, extracting the value from the message.
	            handler.addAction(new PropertySetter("ticketNr", "//req:TicketNr/text()"));
	            
	            // Add a XSLT transformation action. 
	            handler.addAction(new XsltTransformer("resources/request.xsl"));
	    
	            // Add another Log action.
	            handler.addAction(new LogAction(Level.INFO));
	            
	            // Add a FileOut action.
	            FileOut fileOutAction = new FileOut(
	            		"data/hist", "concat('request-', $ticketNr, '.xml')");
	            handler.addAction(fileOutAction);
	            
//	            // Add a HttpOut action.
//	            HttpOutAction httpOutAction = new HttpOutAction("http://localhost:8080/");
//	            handler.addAction(httpOutAction);
	            
	            handlers.add(handler);
	            
	        } catch (CourierException e) {
	            LOG.fatal("Error parsing configuration file", e);
	            System.exit(5);
	        }
    	}
    }
    
}
