package org.ozsoft.webdav.server.method;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ozsoft.webdav.server.WebDavBackend;

/**
 * HTTP POST method.
 * 
 * This method has no meaning in a WebDAV context.
 * 
 * @author Oscar Stigter
 */
public class PostMethod extends AbstractMethod {

	/** Method name. */
	private static final String NAME = "POST";
	
	/**
	 * Constructor.
	 * 
	 * @param servletContext
	 *            The servlet context.
	 * @param backend
	 *            The WebDAV backend.
	 */
	public PostMethod(String servletContext, WebDavBackend backend) {
		super(NAME, servletContext, backend);
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.method.Method#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.process(request, response);
	}

}
