package org.ozsoft.webdav.server.method;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.ozsoft.webdav.WebDavException;
import org.ozsoft.webdav.WebDavStatus;
import org.ozsoft.webdav.server.WebDavBackend;

/**
 * HTTP GET method.
 * 
 * @author Oscar Stigter
 */
public class GetMethod extends AbstractMethod {

	/** Method name. */
	private static final String NAME = "GET";
	
	/** Logger. */
	private static final Logger LOG = Logger.getLogger(GetMethod.class);

	/**
	 * Constructor.
	 * 
	 * @param servletContext
	 *            The servlet context.
	 * @param backend
	 *            The WebDAV backend.
	 */
	public GetMethod(String servletContext, WebDavBackend backend) {
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
			if (backend.exists(uri)) {
				if (backend.isCollection(uri)) {
					response.setContentType("text/plain");
					Writer writer = response.getWriter();
					writer.write(String.format(
							"Contents of collection '%s':\n", uri));
					String[] children = backend.getChildrenNames(uri);
					if (children.length == 0) {
						writer.write("(Empty collection)\n");
					} else {
						for (String child : children) {
							writer.write(String.format("    %s\n", child));
						}
					}
				} else {
					String type = backend.getContentType(uri);
					response.setContentType(type);
					long length = backend.getContentLength(uri);
					response.setContentLength((int) length);
					if (length > 0) {
						InputStream is = backend.getContent(uri);
						ServletOutputStream os = response.getOutputStream();
						byte[] buffer = new byte[8192];
						int read = 0;
						while ((read = is.read(buffer)) > 0) {
							os.write(buffer, 0, read);
						}
						is.close();
					}
				}
				response.setStatus(WebDavStatus.OK.getCode());
			} else {
				response.setContentType("text/plain");
				Writer writer = response.getWriter();
				writer.write("Resource not found: " + uri);
				response.setStatus(WebDavStatus.NOT_FOUND.getCode());
			}
		} catch (WebDavException e) {
			LOG.error(String.format("Error retrieving resource '%s'", uri), e);
			response.setContentType("text/plain");
			Writer writer = response.getWriter();
			writer.write(String.format("Error while retrieving resource '%s': %s\n", uri, e));
		}
	}

}
