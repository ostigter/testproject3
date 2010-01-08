package org.ozsoft.webdav.server.method;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.ozsoft.webdav.WebDavException;
import org.ozsoft.webdav.WebDavStatus;
import org.ozsoft.webdav.server.WebDavBackend;

public class MoveMethod extends AbstractMethod {

	/** Method name. */
	private static final String NAME = "MOVE";
	
	/** Logger. */
	private static final Logger LOG = Logger.getLogger(MoveMethod.class);

	/**
	 * Constructor.
	 * 
	 * @param servletContext
	 *            The servlet context.
	 * @param backend
	 *            The WebDAV backend.
	 */
	public MoveMethod(String servletContext, WebDavBackend backend) {
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
		String destination = request.getHeader("destination");
		if (destination == null) {
			// TODO: Invalid request; no destination header.
		} else {
			if (servletContext != null && servletContext.length() > 0) {
				int pos = destination.indexOf(servletContext);
				if (pos != -1) {
					destination = destination.substring(pos + servletContext.length());
				}
			}
			try {
				backend.move(uri, destination);
				if (LOG.isDebugEnabled()) {
					LOG.debug(String.format("Moved resource '%s' to '%s'", uri, destination));
				}
				response.setStatus(HttpServletResponse.SC_OK);
			} catch (WebDavException e) {
				response.sendError(WebDavStatus.INVALID_REQUEST.getCode(), e.getMessage());
			}
		}
	}

}
