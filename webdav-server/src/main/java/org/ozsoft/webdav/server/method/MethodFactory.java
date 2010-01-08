package org.ozsoft.webdav.server.method;

import java.util.HashMap;
import java.util.Map;

import org.ozsoft.webdav.server.WebDavBackend;

/**
 * Method factory.
 * 
 * @author Oscar Stigter
 */
public class MethodFactory {
	
	/** Unsupported method. */
	private static final Method UNSUPPORTED_METHOD = new UnsupportedMethod(null, null);
	
	/** Methods mapped by name. */
	private final Map<String, Method> methods;
	
	/**
	 * Constructor.
	 * 
	 * @param servletContext
	 *            The servlet context.
	 * @param backend
	 *            The WebDAV backend.
	 */
	public MethodFactory(String servletContext, WebDavBackend backend) {
		methods = new HashMap<String, Method>();
		methods.put("OPTIONS", new OptionsMethod(servletContext, backend));
		methods.put("HEAD", new HeadMethod(servletContext, backend));
		methods.put("GET", new GetMethod(servletContext, backend));
		methods.put("PROPFIND", new PropFindMethod(servletContext, backend));
	}
	
	/**
	 * Returns a method.
	 * 
	 * @param name
	 *            The method name.
	 * 
	 * @return The method if found, otherwise null.
	 */
	public Method getMethod(String name) {
		Method method = methods.get(name);
		if (method != null) {
			return method;
		} else {
			return UNSUPPORTED_METHOD;
		}
	}

}
