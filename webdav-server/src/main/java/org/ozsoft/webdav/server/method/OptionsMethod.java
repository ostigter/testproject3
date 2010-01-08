package org.ozsoft.webdav.server.method;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ozsoft.webdav.WebDavStatus;
import org.ozsoft.webdav.server.WebDavBackend;

/**
 * HTTP OPTIONS method.
 * 
 * @author Oscar Stigter
 */
public class OptionsMethod extends AbstractMethod {
	
	/** Method name. */
	private static final String NAME = "OPTIONS";
	
	/** WebDAV compatibility level. */
	private static final String DAV_LEVEL = "1";
	
	/** Allowed methods. */
	private static final String ALLOWED_METHODS =
			"OPTIONS, HEAD, GET, PUT, DELETE, POST, PROPFIND, MKCOL, COPY, MOVE";
	
	/**
	 * Constructor.
	 * 
	 * @param servletContext
	 *            The servlet context.
	 * @param backend
	 *            The WebDAV backend.
	 */
	public OptionsMethod(String servletContext, WebDavBackend backend) {
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
			response.addHeader("Allow", ALLOWED_METHODS);
			response.addHeader("DAV", DAV_LEVEL);
		} else {
			response.setStatus(WebDavStatus.NOT_FOUND.getCode());
		}
	}

}
