package org.ozsoft.webdav.server;

import java.io.InputStream;
import java.util.Date;

/**
 * Backend for a WebDAV server.
 * 
 * @author Oscar Stigter
 */
public interface WebDavBackend {
	
	boolean exists(String uri);
	
	boolean isCollection(String uri);
	
	String[] getChildrenNames(String uri);
	
	void createCollection(String uri) throws WebDavException;
	
	void createResource(String uri) throws WebDavException;
	
	long getContentLength(String uri);
	
	InputStream getContent(String uri) throws WebDavException;
	
	void setContent(String uri, InputStream content, String contentType, String encoding) throws WebDavException;
	
	Date getCreated(String uri) throws WebDavException;

	Date getLastModified(String uri) throws WebDavException;
	
	void delete(String uri) throws WebDavException;
	
}
