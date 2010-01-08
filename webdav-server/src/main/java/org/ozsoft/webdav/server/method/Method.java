package org.ozsoft.webdav.server.method;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HTTP method handling a request.
 * 
 * @author Oscar Stigter
 */
public interface Method {
	
	/**
	 * Processes the request.
	 * 
	 * @param request
	 *            The request.
	 * @param response
	 *            The response.
	 * 
	 * @throws ServletException
	 *             If the request could not be processed.
	 * @throws IOException
	 *             If an I/O error occurred.
	 */
	void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
