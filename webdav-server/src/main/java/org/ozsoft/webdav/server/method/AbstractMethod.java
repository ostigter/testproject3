package org.ozsoft.webdav.server.method;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.ozsoft.webdav.Depth;
import org.ozsoft.webdav.server.WebDavBackend;

/**
 * Abstract HTTP method.
 * 
 * Contains several utility methods and performs logging.
 * 
 * @author Oscar Stigter
 */
public abstract class AbstractMethod implements Method {
	
	/** Logger. */
	private static final Logger LOG = Logger.getLogger(AbstractMethod.class);
	
	/** Method name. */
	private final String name;

	/** Servlet context. */
	protected final String servletContext;
	
	/** WebDAV backend. */
	protected final WebDavBackend backend;
	
	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The method name.
	 * @param servletContext
	 *            The servlet context.
	 * @param backend
	 *            The WebDAV backend.
	 */
	public AbstractMethod(String name, String servletContext, WebDavBackend backend) {
		this.name = name;
		this.servletContext = servletContext;
		this.backend = backend;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.method.Method#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Method: " + name);
			LOG.debug("URI: " + getUriFromRequest(request));
		}
		if (LOG.isTraceEnabled()) {
			for (Enumeration<?> e = request.getHeaderNames(); e.hasMoreElements();) {
				String name = (String) e.nextElement();
				String value = request.getHeader(name);
				LOG.trace("Request header: " + name + ": " + value);
			}
		}
	}
	
	/**
	 * Returns the request URI, trimming the servlet context.
	 * 
	 * @param request
	 *            The request.
	 * @param servletContext
	 *            The servlet context.
	 * 
	 * @return The request URI.
	 */
	protected final String getUriFromRequest(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if (servletContext.length() != 0) {
			if (uri.startsWith(servletContext)) {
				uri = uri.substring(servletContext.length());
			}
		}
		if (uri.length() == 0) {
			uri = "/";
		}
		return uri;
	}

	/**
	 * Returns the value of the Depth header from a request.
	 * 
	 * @param request
	 *            The request.
	 * 
	 * @return The Depth value.
	 */
	protected final Depth getDepth(HttpServletRequest request) {
		Depth depth = Depth.INFINITY;
		String value = request.getHeader("Depth");
		if (value != null) {
			depth = Depth.parse(value);
			if (depth == null) {
				depth = Depth.INFINITY;
			}
		}
		if (LOG.isTraceEnabled()) {
			LOG.trace("Depth: " + depth);
		}
		return depth;
	}
	
	/**
	 * Returns the request body from an HTTP request.
	 * 
	 * @param request
	 *            The request.
	 * 
	 * @return The request body.
	 * 
	 * @throws IOException
	 *             If the request could not be read.
	 */
	protected final String getRequestBody(HttpServletRequest request) throws IOException {
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append('\n');
		}
		String requestBody = sb.toString();
		if (LOG.isTraceEnabled() && requestBody.length() > 0) {
			LOG.trace("Request body:\n" + requestBody);
		}
		return requestBody;
	}

	/**
	 * Returns the resource name from a resource URI.
	 * 
	 * @param uri
	 *            The resource URI.
	 * 
	 * @return The resource name.
	 */
	protected final String getResourceName(String uri) {
		String name = uri;
		if (name.length() > 1) {
			int pos = name.lastIndexOf('/');
			if (pos != -1) {
				name = name.substring(pos + 1);
			}
		}
		return name;
	}

}
