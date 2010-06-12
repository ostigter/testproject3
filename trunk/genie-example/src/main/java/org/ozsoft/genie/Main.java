package org.ozsoft.genie;

import org.ozsoft.genie.GenieClient;
import org.ozsoft.genie.GenieException;
import org.ozsoft.genie.GenieServer;
import org.ozsoft.genie.calculator.CalculatorImpl;

/**
 * A example application with a Genie-based calculator service.
 * 
 * This example implements both the server and the client side. 
 * 
 * @author Oscar Stigter
 */
public class Main {

	public static void main(String[] args) {
		GenieServer server = null;
		
		// Create the Calculator service handler.
		CalculatorImpl calculator = new CalculatorImpl();

		try {
			// Create a Genie server on the default port.
			server = new GenieServer();
			
			// Bind the named Calculator service to the service handler.
			server.bind("Calculator", calculator);
			
			// Start the Genie server.
			server.start();
			
			// Create a Genie client for the Calculator service.
			GenieClient client = new GenieClient("localhost", "Calculator");
			
			// Execute a Calculator service call and print the result.
			int result = (Integer) client.call("add", 2, 3);
			System.out.println("Result: " + result);
			
		} catch (GenieException e) {
			System.err.println("ERROR: " + e.getMessage());
			
		} finally {
			// Stop the Genie server.
			server.stop();
		}
	}

}
