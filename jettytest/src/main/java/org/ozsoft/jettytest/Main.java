package org.ozsoft.jettytest;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;

/**
 * Test driver for an embedded Jetty 6 web server.
 * 
 * Browse to the HelloServlet at "http://localhost:8088/hello"
 * and to the DummyServlet at "http://localhost:8088/dummy".
 * 
 * @author Oscar Stigter
 */
public class Main {
	
	/** The log. */
	private static final Logger LOG = Logger.getLogger(Main.class);
	
	/** The port. */
	private static final int PORT = 8088;

	/**
	 * Application's entry point.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		LOG.debug("Starting");
		
		Server server = new Server(PORT);
		
		Context context = new Context(server, "/", Context.SESSIONS);
		context.addServlet(HelloServlet.class, "/hello/*");
		context.addServlet(DummyServlet.class, "/dummy/*");
		
		try {
			server.start();
			LOG.info(String.format("Started; listening on port %d", PORT));
		} catch (Exception e) {
			LOG.error("Could not start server", e);
		}
	}

}
