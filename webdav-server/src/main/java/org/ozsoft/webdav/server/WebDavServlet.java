package org.ozsoft.webdav.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.ozsoft.webdav.Depth;
import org.ozsoft.webdav.PropStat;
import org.ozsoft.webdav.WebDavConstants;
import org.ozsoft.webdav.WebDavException;
import org.ozsoft.webdav.WebDavResponse;
import org.ozsoft.webdav.WebDavStatus;

/**
 * HTTP servlet handling WebDAV requests.
 * 
 * Only the basic WebDAV level 1 operations are supported.
 * 
 * @author Oscar Stigter
 */
public class WebDavServlet extends HttpServlet {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	/** The log. */
	private static final Logger LOG = Logger.getLogger(WebDavServlet.class);
	
	/** The servlet context. */
	private final String context;
	
	/** The WebDAV backend. */
	private final WebDavBackend backend;
	
	/**
	 * Constructor.
	 * 
	 * @param backend
	 *            The WebDAV backend.
	 */
	public WebDavServlet(String context, WebDavBackend backend) {
		if (context == null) {
			throw new IllegalArgumentException("Null context");
		}
        if (backend == null) {
            throw new IllegalArgumentException("Null backend");
        }
        this.context = context;
		this.backend = backend;
	}

    /**
     * Handles an HTTP request.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String method = request.getMethod();
//		for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
//			String name = (String) e.nextElement();
//			String value = request.getHeader(name);
//			LOG.debug("Header: " + name + ": " + value);
//		}

		if (method.equals("OPTIONS")) {
			doOptions(request, response);
		} else if (method.equals("PROPFIND")) {
			doPropfind(request, response);
		} else if (method.equals("HEAD")) {
			doHead(request, response);
		} else if (method.equals("GET")) {
			doGet(request, response);
		} else if (method.equals("PUT")) {
			doPut(request, response);
		} else if (method.equals("DELETE")) {
			doDelete(request, response);
		} else if (method.equals("POST")) {
			doPost(request, response);
		} else {
			LOG.warn("Unsupported HTTP method: " + method);
			response.setStatus(400);
		}
	}

    /**
     * Handles an OPTIONS request.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     */
	@Override
	protected void doOptions(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		super.doOptions(request, response);
		LOG.debug("OPTIONS");
		response.setStatus(200);
		response.addHeader("Allow", "OPTIONS, HEAD, GET, PUT, DELETE, POST, PROPFIND, MKCOL");
		response.addHeader("DAV", "1");
//		response.addHeader("MS-Author-Via", "DAV");  // Required for Microsoft Windows Explorer?
	}

    /**
     * Handles a PROPFIND request.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     */
	@SuppressWarnings("unchecked")  // dom4j
	private void doPropfind(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = getUriFromRequest(request);
		LOG.debug("PROPFIND  " + uri);
		Depth depth = getDepth(request);
		List<WebDavResponse> responses = new ArrayList<WebDavResponse>();
		WebDavResponse webDavResponse = new WebDavResponse(uri);
		responses.add(webDavResponse);
		String requestBody = getRequestBody(request);
		if (requestBody.length() == 0) {
			// No request body, so handle as 'allprop' type.
			doPropfindAllprop(uri, depth, responses);
		} else {
			// Parse request body.
			try {
				Document doc = DocumentHelper.parseText(requestBody);
				Element root = doc.getRootElement();
				if (root.getNamespaceURI().equals(WebDavConstants.DAV_NS) && root.getName().equals("propfind")) {
					List children = root.elements();
					if (children.size() == 1) {
						Element typeElement = (Element) children.get(0);
						String typeName = typeElement.getName();
						if (typeName.equals("prop")) {
							// Request for specific set of properties.
							LOG.debug("Request for a specific set of properties");
							for (Object obj : typeElement.elements()) {
								if (obj instanceof Element) {
									Element el = (Element) obj;
									//TODO: Handle request for non-WebDAV properties. 
									if (el.getNamespaceURI().equals(WebDavConstants.DAV_NS)) {
									    webDavResponse.addProperty(el.getName());
									}
								} else {
									//TODO: Child of 'prop' element not an element.
								}
							}
							if (webDavResponse.getPropStats().size() == 0) {
								//TODO: Invalid request; no (WebDAV) properties specified.
							} else {
								doPropfindProp(uri, depth, responses);
							}
						} else if (typeName.equals("allprop")) {
							// Request for all properties (incl. values).
							doPropfindAllprop(uri, depth, responses);
						} else if (typeName.equals("propname")) {
							// Request for all property names.
							doPropfindPropname(uri, depth, responses);
						} else {
							//TODO: Invalid request; invalid child element of 'propfind' element.
						}
					} else {
						//TODO: Invalid request; 'propfind' element has invalid number of child elements. 
					}
				} else {
					//TODO: Invalid request; no 'propfind' element.
				}
			} catch (DocumentException e) {
				//TODO: Invalid XML request body.
				LOG.error("Could not parse request", e);
			} catch (WebDavException e) {
			    LOG.error(e);
			}
		}

//		// Response status and headers.
//		response.setStatus(207); // Multi-Status
//		response.setContentType("text/xml;charset=UTF-8");
//		response.addHeader("DAV", "1");
//		response.addHeader("MS-Author-Via", "DAV");
	}
	
    /**
     * Handles a PROPFIND 'prop' request.
     * 
     * @param uri
     *            The resource URI.
     * @param depth
     *            The Depth header value.
     * @param propNames
     *            The property names.
     * @param responses
     *            The property responses.
     */
	private void doPropfindProp(String uri, Depth depth, List<WebDavResponse> responses)
			throws ServletException, IOException, WebDavException {
	    if (backend.exists(uri)) {
            doPropfindProp(uri, responses);
            if (backend.isCollection(uri)) {
                if (depth != Depth.ZERO) {
                    String[] children = backend.getChildrenNames(uri);
                    for (String child : children) {
                        String childUri = (uri.endsWith("/")) ? uri + child : uri + "/" + child;
                        if (depth == Depth.ONE){
                            doPropfindProp(childUri, responses);
                        } else {
                            doPropfindProp(childUri, depth, responses);
                        }
                    }
                }
            }
	    } else {
	        //TODO: Resource not found.
	    }
	}
	
    /**
     * Handles a PROPFIND 'prop' request for a specific resource.
     * 
     * @param uri
     *            The resource URI.
     * @param propNames
     *            The property names.
     * @param responses
     *            The resource responses.
     */
	private void doPropfindProp(String uri, List<WebDavResponse> responses)
	        throws ServletException, IOException, WebDavException {
        if (backend.exists(uri)) {
            for (WebDavResponse response : responses) {
                for (PropStat propStat : response.getPropStats()) {
                    String propName = propStat.getName();
                    if (propName.equals(WebDavConstants.DISPLAY_NAME)) {
                        // Display name.
                        propStat.setValue(uri);
                        propStat.setStatus(WebDavStatus.OK);
                    } else if (propName.equals(WebDavConstants.RESOURCE_TYPE)) {
                        // Resource type.
                        if (backend.isCollection(uri)) {
                            propStat.setValue(WebDavConstants.COLLECTION);
                        } else {
                            propStat.setValue(WebDavConstants.RESOURCE);
                        }
                        propStat.setStatus(WebDavStatus.OK);
                    } else if (propName.equals(WebDavConstants.CONTENT_TYPE)) {
                        // Content type.
                        if (!backend.isCollection(uri)) {
                            propStat.setValue(backend.getContentType(uri));
                            propStat.setStatus(WebDavStatus.OK);
                        } else {
                            propStat.setStatus(WebDavStatus.NOT_FOUND);
                        }
                    } else if (propName.equals(WebDavConstants.CONTENT_LENGTH)) {
                        // Content length.
                        if (!backend.isCollection(uri)) {
                            propStat.setValue(String.valueOf(backend.getContentLength(uri)));
                            propStat.setStatus(WebDavStatus.OK);
                        } else {
                            propStat.setStatus(WebDavStatus.NOT_FOUND);
                        }
                    } else if (propName.equals(WebDavConstants.CREATED)) {
                        // Creation date.
                        //FIXME: Use ISO date format.
                        propStat.setValue(backend.getCreated(uri).toString());
                        propStat.setStatus(WebDavStatus.OK);
                    } else if (propName.equals(WebDavConstants.MODIFIED)) {
                        // Creation date.
                        //FIXME: Use ISO date format.
                        propStat.setValue(backend.getModified(uri).toString());
                        propStat.setStatus(WebDavStatus.OK);
                    } else {
                        // Unknown property.
                        propStat.setStatus(WebDavStatus.NOT_FOUND);
                    }
                }
            }
        } else {
            //TODO: Resource not found.
        }
    }

    /**
     * Handles a PROPFIND 'allprop' request.
     * 
     * @param uri
     *            The resource URI.
     * @param depth
     *            The Depth header value.
     * @param responses
     *            The property responses.
     */
	private void doPropfindAllprop(String uri, Depth depth, List<WebDavResponse> responses)
			throws ServletException, IOException {
		//TODO
	}

    /**
     * Handles a PROPFIND 'propname' request.
     * 
     * @param uri
     *            The resource URI.
     * @param depth
     *            The Depth header value.
     * @param responses
     *            The property responses.
     */
	private void doPropfindPropname(String uri, Depth depth, List<WebDavResponse> responses)
			throws ServletException, IOException {
		//TODO
	}

    /**
     * Handles a HEAD request.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     */
	@Override
	protected void doHead(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		LOG.debug("HEAD " + uri);
	}

    /**
     * Handles a GET request.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		LOG.debug("GET " + uri);
	}

    /**
     * Handles a PUT request.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     */
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		LOG.debug("PUT " + uri);
	}

    /**
     * Handles a DELETE request.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		LOG.debug("DELETE " + uri);
	}
	
    /**
     * Handles a POST request.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getPathInfo();
		LOG.debug("POST " + uri);
	}

    /**
     * Returns the request URI, trimming the servlet context.
     * 
     * @param request
     *            The request.
     * 
     * @return The request URI.
     */
    private String getUriFromRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (context.length() != 0) {
            if (uri.startsWith(context)) {
                uri = uri.substring(context.length());
            }
        }
        if (uri.length() == 0) {
            uri = "/";
        }
        return uri;
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
	private String getRequestBody(HttpServletRequest request) throws IOException {
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append('\n');
		}
		String requestBody = sb.toString();
		if (requestBody.length() != 0) {
			LOG.debug("Request body:\n" + requestBody);
		}
		return requestBody;
	}
	
	/**
	 * Returns the value of the Depth header from a request.
	 * 
	 * @param request
	 *            The request.
	 * 
	 * @return The Depth value.
	 */
	private Depth getDepth(HttpServletRequest request) {
		Depth depth = Depth.INFINITY;
		String value = request.getHeader("Depth");
		if (value != null) {
			depth = Depth.parse(value);
			if (depth == null) {
				depth = Depth.INFINITY;
			}
		}
		LOG.debug("Depth: " + depth);
		return depth;
	}
	
}
