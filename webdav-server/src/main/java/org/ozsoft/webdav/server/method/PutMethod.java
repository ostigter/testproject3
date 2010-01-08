package org.ozsoft.webdav.server.method;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.ozsoft.webdav.WebDavException;
import org.ozsoft.webdav.WebDavStatus;
import org.ozsoft.webdav.server.WebDavBackend;

public class PutMethod extends AbstractMethod {

	/** Method name. */
	private static final String NAME = "PUT";
	
	/** Logger. */
	private static final Logger LOG = Logger.getLogger(PutMethod.class);

	/**
	 * Constructor.
	 * 
	 * @param servletContext
	 *            The servlet context.
	 * @param backend
	 *            The WebDAV backend.
	 */
	public PutMethod(String name, String servletContext, WebDavBackend backend) {
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
			String contentType = request.getContentType();
			String encoding = request.getCharacterEncoding();
			if (!backend.exists(uri)) {
				backend.createResource(uri);
			}
			InputStream content = (InputStream) request.getInputStream();
			backend.setContent(uri, content, contentType, encoding);
			response.setStatus(WebDavStatus.OK.getCode());
		} catch (WebDavException e) {
			LOG.error(String.format("Error storing resource '%s'", uri), e);
			response.sendError(WebDavStatus.INVALID_REQUEST.getCode(), e.getMessage());
		}
	}

}
