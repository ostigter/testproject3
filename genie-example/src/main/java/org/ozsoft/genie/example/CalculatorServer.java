package org.ozsoft.genie.example;

import org.ozsoft.genie.GenieConstants;
import org.ozsoft.genie.GenieException;
import org.ozsoft.genie.GenieServer;

/**
 * Genie-based Calculator service.
 * 
 * The first (optional) argument specifies the Genie server port.
 * 
 * @author Oscar Stigter
 */
public class CalculatorServer {
	
	public static void main(String[] args) {
		int port = (args.length >= 1) ?
				Integer.parseInt(args[0]) : GenieConstants.DEFAULT_PORT;
		try { 
			GenieServer server = new GenieServer(port);
			server.bind("Calculator", new CalculatorImpl());
			server.start();
			System.out.println("Genie server started (Press CTRL+C to quit)");
		} catch (GenieException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
