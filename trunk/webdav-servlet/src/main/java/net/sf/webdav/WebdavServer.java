package net.sf.webdav;

import javax.servlet.Servlet;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * Standalone WebDAV server powered by Jetty.
 * 
 * @author Oscar Stigter
 */
public class WebdavServer {

	/** The log. */
	private static final Logger LOG = Logger.getLogger(WebdavServer.class);
	
	/** The port to listen to. */
	private static final int PORT = 8088;

	/** The servlet context. */
	private static final String CONTEXT = "/webdav/*";
	
	public static void main(String[] args) {
		Server server = new Server(PORT);
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(WebdavServlet.class, CONTEXT);
		server.setHandler(handler);
		try {
			server.start();
			LOG.info(String.format("Started, listening on port %d", PORT));
		} catch (Exception e) {
			LOG.error("Could not start server", e);
		}
	}

}
