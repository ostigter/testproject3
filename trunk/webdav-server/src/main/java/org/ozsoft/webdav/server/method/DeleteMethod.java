package org.ozsoft.webdav.server.method;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.ozsoft.webdav.WebDavException;
import org.ozsoft.webdav.WebDavStatus;
import org.ozsoft.webdav.server.WebDavBackend;

public class DeleteMethod extends AbstractMethod {

	/** Method name. */
	private static final String NAME = "DELETE";
	
	/** Logger. */
	private static final Logger LOG = Logger.getLogger(DeleteMethod.class);

	/**
	 * Constructor.
	 * 
	 * @param servletContext
	 *            The servlet context.
	 * @param backend
	 *            The WebDAV backend.
	 */
	public DeleteMethod(String servletContext, WebDavBackend backend) {
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
		try {
			backend.delete(uri);
			response.setStatus(WebDavStatus.OK.getCode());
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Deleted resource '%s'", uri));
			}
		} catch (WebDavException e) {
			LOG.error(String.format("Error deleting resource '%s'", uri), e);
			response.sendError(WebDavStatus.INVALID_REQUEST.getCode(), e.getMessage());
		}
	}

}
