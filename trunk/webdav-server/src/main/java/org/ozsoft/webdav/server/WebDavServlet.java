package org.ozsoft.webdav.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ozsoft.webdav.server.method.Method;
import org.ozsoft.webdav.server.method.MethodFactory;

/**
 * HTTP servlet handling WebDAV requests.
 * 
 * Only WebDAV level 1 operations are supported.
 * 
 * @author Oscar Stigter
 */
public class WebDavServlet extends HttpServlet {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** Method factory. */
	private final MethodFactory methodFactory;
	
	/**
	 * Constructor.
	 * 
	 * @param backend
	 *            The WebDAV backend.
	 */
	public WebDavServlet(String context, WebDavBackend backend) {
		if (context == null) {
			throw new IllegalArgumentException("Null context");
		}
		if (backend == null) {
			throw new IllegalArgumentException("Null backend");
		}
		methodFactory = new MethodFactory(context, backend);
	}

	/**
	 * Handles an HTTP request.
	 * 
	 * @param request
	 *            The request.
	 * @param response
	 *            The response.
	 */
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Method method = methodFactory.getMethod(request.getMethod());
		method.process(request, response);
	}

}
