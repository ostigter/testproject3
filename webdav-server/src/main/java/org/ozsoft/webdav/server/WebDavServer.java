package org.ozsoft.webdav.server;

import javax.servlet.Servlet;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * Standalone WebDAV server with a simple filesystem backend.
 * 
 * Uses a WebDAV servlet deployed in an embedded Jetty.
 * 
 * Connection URL: "http://localhost:8088/webdav/".
 * 
 * @author Oscar Stigter
 */
public class WebDavServer {
	
	/** The log. */
	private static final Logger LOG = Logger.getLogger(WebDavServer.class);
	
	/** The port to listen to. */
	private static final int PORT = 8088;

	/** The servlet context. */
	private static final String CONTEXT = "/webdav";
	
	/** Root directory of the file system backend. */
	private static final String ROOT_DIR = "data";

	/**
	 * The application's entry point.
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		Server server = new Server(PORT);
		ServletHandler handler = new ServletHandler();
		WebDavBackend backend = new FileSystemBackend(ROOT_DIR);
		Servlet servlet = new WebDavServlet(CONTEXT, backend);
		handler.addServletWithMapping(new ServletHolder(servlet), CONTEXT + "/*");
		server.setHandler(handler);
		try {
			server.start();
			LOG.info(String.format("Started, listening on port %d", PORT));
		} catch (Exception e) {
			LOG.error("Could not start server", e);
		}
	}

}
