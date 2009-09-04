package org.ozsoft.webdav.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
 * Only basic WebDAV level 1 operations are supported.
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
		LOG.debug("Method: " + method);
		for (Enumeration e = request.getHeaderNames(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			String value = request.getHeader(name);
			LOG.debug("Header: " + name + ": " + value);
		}
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
        } else if (method.equals("MKCOL")) {
            doMkCol(request, response);
        } else if (method.equals("MOVE")) {
            doMove(request, response);
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
		LOG.debug("OPTIONS");
		response.setStatus(WebDavStatus.OK.getCode());
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
							List<String> propNames = new ArrayList<String>();
							for (Object obj : typeElement.elements()) {
								if (obj instanceof Element) {
									Element el = (Element) obj;
									//TODO: Handle request for non-WebDAV properties. 
									if (el.getNamespaceURI().equals(WebDavConstants.DAV_NS)) {
									    propNames.add(el.getName());
									}
								} else {
									//TODO: Child of 'prop' element not an element.
								}
							}
							if (propNames.size() == 0) {
								//TODO: Invalid request; no (WebDAV) properties specified.
							} else {
								doPropfindProp(uri, depth, propNames, responses);
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
				
				StringBuilder sb = new StringBuilder();
				sb.append(WebDavConstants.XML_DECLARATION);
				sb.append("<multistatus xmlns=\"").append(WebDavConstants.DAV_NS).append("\">\n");
				for (WebDavResponse r : responses) {
	                sb.append("  <response>\n");
	                String href = context + r.getUri();
	                if (href.endsWith("/")) {
	                    href = href.substring(0, href.length() - 1);
	                }
	                sb.append("    <href>").append(href).append("</href>\n");
	                for (PropStat propStat : r.getPropStats()) {
	                    sb.append("    <propstat>\n");
                        sb.append("      <prop>\n");
                        String propName = propStat.getName();
                        sb.append("        <").append(propName).append(">");
                        String propValue = propStat.getValue();
                        if (propValue != null) {
                            sb.append(propValue);
                        }
                        sb.append("</").append(propName).append(">\n");
                        sb.append("      </prop>\n");
                        WebDavStatus status = propStat.getStatus();
                        sb.append("      <status>HTTP/1.1 ").append(status.getCode());
                        sb.append(' ').append(status.getName()).append("</status>\n");
                        sb.append("    </propstat>\n");
	                }
                    sb.append("  </response>\n");
				}
                sb.append("</multistatus>\n");
                String responseBody = sb.toString();
//                LOG.debug("Response:\n" + responseBody);
                response.getWriter().write(responseBody);
			} catch (DocumentException e) {
				//TODO: Invalid XML request body.
				LOG.error("Could not parse request", e);
			} catch (WebDavException e) {
			    LOG.error(e);
			}
		}
		
		// Response status and headers.
		response.setStatus(WebDavStatus.MULTI_STATUS.getCode());
		response.setContentType(WebDavConstants.CONTENT_TYPE);
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
	private void doPropfindProp(String uri, Depth depth, List<String> propNames, List<WebDavResponse> responses)
			throws ServletException, IOException, WebDavException {
	    if (backend.exists(uri)) {
            doPropfindProp(uri, propNames, responses);
            if (backend.isCollection(uri)) {
                if (depth != Depth.ZERO) {
                    String[] children = backend.getChildrenNames(uri);
                    for (String child : children) {
                        String childUri = (uri.endsWith("/")) ? uri + child : uri + "/" + child;
                        if (depth == Depth.ONE){
                            doPropfindProp(childUri, propNames, responses);
                        } else {
                            doPropfindProp(childUri, depth, propNames, responses);
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
	private void doPropfindProp(String uri, List<String> propNames, List<WebDavResponse> responses)
	        throws ServletException, IOException, WebDavException {
        if (backend.exists(uri)) {
            WebDavResponse response = new WebDavResponse(uri);
            for (String propName : propNames) {
                PropStat propStat = new PropStat(propName);
                if (propName.equals(WebDavConstants.DISPLAYNAME)) {
                    // Display name.
                    propStat.setValue(getResourceName(uri));
                    propStat.setStatus(WebDavStatus.OK);
                } else if (propName.equals(WebDavConstants.RESOURCETYPE)) {
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
                } else if (propName.equals(WebDavConstants.GETCONTENTLENGTH)) {
                    // Content length.
                    if (!backend.isCollection(uri)) {
                        propStat.setValue(String.valueOf(backend.getContentLength(uri)));
                        propStat.setStatus(WebDavStatus.OK);
                    } else {
                        propStat.setStatus(WebDavStatus.NOT_FOUND);
                    }
                } else if (propName.equals(WebDavConstants.CREATIONDATE)) {
                    // Creation date.
                    //FIXME: Use ISO date format.
                    propStat.setValue(backend.getCreated(uri).toString());
                    propStat.setStatus(WebDavStatus.OK);
                } else if (propName.equals(WebDavConstants.GETLASTMODIFIED)) {
                    // Creation date.
                    //FIXME: Use ISO date format.
                    propStat.setValue(backend.getModified(uri).toString());
                    propStat.setStatus(WebDavStatus.OK);
                } else {
                    // Unknown property.
                    propStat.setStatus(WebDavStatus.NOT_FOUND);
                }
                response.addPropStat(propStat);
            }
            responses.add(response);
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
		if (backend.exists(uri)) {
	        response.setStatus(WebDavStatus.OK.getCode());
		} else {
            response.setStatus(WebDavStatus.NOT_FOUND.getCode());
		}
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
		String uri = getUriFromRequest(request);
		LOG.debug("GET " + uri);
		try {
    		if (backend.exists(uri)) {
    	        if (backend.isCollection(uri)) {
    	            response.setContentType("text/plain");
    	            Writer writer = response.getWriter();
    	            writer.write(String.format("Contents of collection '%s':\n", uri));
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
	        response.setContentType("text/plain");
	        Writer writer = response.getWriter();
		    writer.write(String.format("Error while retrieving resource '%s': %s\n", uri, e));
		}
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
		String uri = getUriFromRequest(request);
		LOG.debug("PUT " + uri);
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
            response.sendError(WebDavStatus.INVALID_REQUEST.getCode(), e.getMessage());
		}
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
        String uri = getUriFromRequest(request);
		LOG.debug("DELETE " + uri);
		try {
		    backend.delete(uri);
            response.setStatus(WebDavStatus.OK.getCode());
		} catch (WebDavException e) {
            response.sendError(WebDavStatus.INVALID_REQUEST.getCode(), e.getMessage());
		}
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
        String uri = getUriFromRequest(request);
		LOG.debug("POST " + uri);
	}
	
    /**
     * Handles a MKCOL request.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     */
	private void doMkCol(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
        String uri = getUriFromRequest(request);
        LOG.debug("MKCOL " + uri);
        try {
            backend.createCollection(uri);
            response.setStatus(WebDavStatus.OK.getCode());
        } catch (WebDavException e) {
            response.sendError(WebDavStatus.INVALID_REQUEST.getCode(), e.getMessage());
        }
	}

    /**
     * Handles a MOVE request.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     */
    private void doMove(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = getUriFromRequest(request);
        LOG.debug("MOVE " + uri);
        String destination = request.getHeader("destination");
        if (destination == null) {
            //TODO: Invalid request; no destination header.
        } else {
            if (context != null && context.length() > 0) {
                int pos = destination.indexOf(context);
                if (pos != -1) {
                    destination = destination.substring(pos + context.length());
                }
            }
            try {
                backend.move(uri, destination);
                response.setStatus(WebDavStatus.OK.getCode());
            } catch (WebDavException e) {
                response.sendError(WebDavStatus.INVALID_REQUEST.getCode(), e.getMessage());
            }
        }
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
     * Returns the resource name from a resource URI.
     * 
     * @param uri
     *            The resource URI.
     * 
     * @return The resource name.
     */
    private static String getResourceName(String uri) {
        String name = uri;
        if (name.length() > 1) {
            int pos = name.lastIndexOf('/');
            if (pos != -1) {
                name = name.substring(pos + 1);
            }
        }
        return name;
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
	private static String getRequestBody(HttpServletRequest request) throws IOException {
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
	private static Depth getDepth(HttpServletRequest request) {
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
