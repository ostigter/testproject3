package org.ozsoft.genie.example;

import org.ozsoft.genie.GenieClient;
import org.ozsoft.genie.GenieConstants;
import org.ozsoft.genie.GenieException;

/**
 * Genie-based Calculator client.
 * 
 * The first (optional) argument specifies the hostname of the Genie server;
 * a second (optional) argument specifies the port.
 * 
 * @author Oscar Stigter
 */
public class CalculatorClient {

	public static void main(String[] args) {
		String host = (args.length >= 1) ?
				args[0] : GenieConstants.DEFAULT_HOST;
		int port = (args.length >= 2) ?
				Integer.parseInt(args[1]) : GenieConstants.DEFAULT_PORT;
		try {
			System.out.println("Calling Genie service...");
			GenieClient client = new GenieClient(host, port, "Calculator");
			Integer result = (Integer) client.call("add", 2, 3);
			System.out.println("Result: " + result);
		} catch (GenieException e) {
			System.err.println("ERROR: " + e.getMessage());
		}
	}

}
