package org.ozsoft.webdav.server.method;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ozsoft.webdav.WebDavStatus;
import org.ozsoft.webdav.server.WebDavBackend;

/**
 * HTTP HEAD method.
 * 
 * @author Oscar Stigter
 */
public class HeadMethod extends AbstractMethod {

	/** Method name. */
	private static final String NAME = "HEAD";
	
	/**
	 * Constructor.
	 * 
	 * @param servletContext
	 *            The servlet context.
	 * @param backend
	 *            The WebDAV backend.
	 */
	public HeadMethod(String servletContext, WebDavBackend backend) {
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
		String uri = getUriFromRequest(request);
		if (backend.exists(uri)) {
			response.setStatus(WebDavStatus.OK.getCode());
		} else {
			response.setStatus(WebDavStatus.NOT_FOUND.getCode());
		}
	}

}
