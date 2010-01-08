package org.ozsoft.webdav.server.method;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.ozsoft.webdav.server.WebDavBackend;

/**
 * Unsupported HTTP method.
 * 
 * @author Oscar Stigter
 */
public class UnsupportedMethod extends AbstractMethod {

	/** Method name. */
	private static final String NAME = "(unsupported)";
	
	/** The log. */
	private static final Logger LOG = Logger.getLogger(UnsupportedMethod.class);
	
	/**
	 * Constructor.
	 * 
	 * @param servletContext
	 *            The servlet context.
	 * @param backend
	 *            The WebDAV backend.
	 */
	public UnsupportedMethod(String servletContext, WebDavBackend backend) {
		super(NAME, servletContext, backend);
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.method.Method#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Unsupported HTTP method '%s'", request.getMethod()));
		}
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

}
