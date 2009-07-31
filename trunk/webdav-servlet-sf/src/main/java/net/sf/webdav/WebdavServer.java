package net.sf.webdav;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;

/**
 * Standalone WebDAV level 2 server powered by Jetty.
 * 
 * @author Oscar Stigter
 */
public class WebdavServer {

	public static void main(String[] args) {
		Server server = new Server(8088);
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(WebdavServlet.class, "/webdav/*");
		server.setHandler(handler);
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

}
