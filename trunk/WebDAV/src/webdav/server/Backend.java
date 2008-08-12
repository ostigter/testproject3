package webdav.server;


import java.io.IOException;
import java.io.OutputStream;

import webdav.Request;


/**
 * A HTTP/WebDAV backend.
 *  
 * @author Oscar Stigter
 */
public interface Backend {
	
	
	/**
	 * Implements the HTTP/WebDAV PROPFIND method, returning one or more named
	 * properties of the resource with the specified URI.
	 * 
	 * @param request   the request
	 * @param response  the response output stream
	 * 
	 * @throws IOException  if the request could not be handled
	 */
	void propFind(Request request, OutputStream response) throws IOException;
	
	
	/**
	 * Implements the HTTP GET method, returning the resource with the
	 * specified URI.
	 * 
	 * @param request   the request
     * @param response  the response output stream
	 * 
	 * @return  the resource
	 * 
	 * @throws IOException  if the request could not be handled
	 */
	void get(Request request, OutputStream response) throws IOException;
	

//	/**
//	 * Implements the HTTP PUT method, storing a resource at the specified URI.
//	 *  
//	 * @param uri  the resource URI
//	 * @param data  the contents of the resource
//	 */
//	void put(String uri, InputStream contents, OutputStream response)
//	        throws IOException;
//	
//	
//	/**
//	 * Implements the HTTP POST method, posting data to the resource with the
//	 * specified URI.
//	 * 
//	 * @param uri   the resource URI
//	 * @param data  the data
//	 */
//	void post(String uri, OutputStream data) throws IOException;
//	
//	
//	/**
//	 * Implements the HTTP DELETE method, deleting the resource with the
//	 * specified URI.
//	 * 
//	 * @param uri  the resource URI
//	 */
//	void delete(String uri); 
//	
//
//	/**
//	 * Implements the WebDAV PROPFIND method, returning one or more named
//	 * properties of the resource with the specified URI.
//	 *  
//	 * @param uri         the resource URI
//	 * @param properties  the names of the specific properties, or null for all
//	 * 
//	 * @return  a map with the requested properties
//	 */
//	Map<String, Object> propFind(String uri, String[] properties);
//	
//	
//	/**
//	 * Implements the WebDAV MKCOL method, creating a collection with the
//	 * specified URI.
//	 * 
//	 * @param uri  the collection URI
//	 */
//	void mkCol(String uri);


}
