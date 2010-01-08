package org.ozsoft.webdav.server;

import java.io.InputStream;
import java.util.Date;

import org.ozsoft.webdav.WebDavException;

/**
 * Backend for a WebDAV server, handling requests on resources and collections.
 * 
 * @author Oscar Stigter
 */
public interface WebDavBackend {
	
	/**
	 * Returns true if a specific resource exists.
	 * 
	 * @param uri
	 *            The resource URI.
	 * 
	 * @return true if the resource exists, otherwise false.
	 */
	boolean exists(String uri);
	
	/**
	 * Returns true if a specific resource is a collection.
	 * 
	 * @param uri
	 *            The resource URI.
	 * 
	 * @return true if a collection, otherwise false.
	 */
	boolean isCollection(String uri);
	
	/**
	 * Returns the names of the resources in a specific collection.
	 * 
	 * @param uri
	 *            The collection URI.
	 * 
	 * @return The names of the resources in the collection.
	 * 
	 * @throws WebDavException
	 *             If the collection does not exist.
	 */
	String[] getChildrenNames(String uri) throws WebDavException;
	
	/**
	 * Creates a collection.
	 * 
	 * All intermediate collection must already exist.
	 * 
	 * @param uri
	 *            The collection URI.
	 * 
	 * @throws WebDavException
	 *             If the collection already exists, any intermediate
	 *             collections do not exist, the user has insufficient
	 *             privileges, or the collection could not be created.
	 */
	void createCollection(String uri) throws WebDavException;
	
	/**
	 * Creates a (non-collection) resource.
	 * 
	 * @param uri
	 *            The resource URI.
	 * 
	 * @throws WebDavException
	 *             If the resource already exists, the parent collection does
	 *             not exist, the user has insufficient privileges, or the
	 *             resource could not be created.
	 */
	void createResource(String uri) throws WebDavException;
	
    /**
     * Returns the content type of a resource.
     * 
     * @param uri
     *            The resource URI.
     * 
     * @return The content type.
     */
    String getContentType(String uri);
    
	/**
	 * Returns the content length of the resource.
	 * 
	 * The content length of a collection is already 0.
	 * 
	 * @param uri
	 *            The resource URI.
	 * 
	 * @return The content length in bytes.
	 */
	long getContentLength(String uri);
	
	/**
	 * Returns the content of a resource.
	 * 
	 * @param uri
	 *            The resource URI.
	 * 
	 * @return The content.
	 * 
	 * @throws WebDavException
	 *             If the resource does not exist, the user has insufficient
	 *             privileges, or the resource could not be read.
	 */
	InputStream getContent(String uri) throws WebDavException;
	
	/**
	 * Sets the content of a (non-collection) resource.
	 * 
	 * @param uri
	 *            The resource URI.
	 * @param content
	 *            The content.
	 * @param contentType
	 *            The content type.
	 * @param encoding
	 *            The content encoding.
	 * 
	 * @throws WebDavException
	 *             If the resource does not exist, the resource is a collection,
	 *             the user has insufficient privileges, or the resource could
	 *             not be written.
	 */
	void setContent(String uri, InputStream content, String contentType, String encoding) throws WebDavException;
	
	/**
	 * Returns the creation date of a resource.
	 * 
	 * @param uri
	 *            The resource URI.
	 * 
	 * @return The creation date.
	 * 
	 * @throws WebDavException
	 *             If the resource does not exist.
	 */
	Date getCreated(String uri) throws WebDavException;

	/**
	 * Returns the modification date of a resource.
	 * 
	 * @param uri
	 *            The resource URI.
	 * 
	 * @return The modification date.
	 * 
	 * @throws WebDavException
	 *             If the resource does not exist.
	 */
	Date getModified(String uri) throws WebDavException;
	
	/**
	 * Deletes a resource.
	 * 
	 * @param uri
	 *            The resource URI.
	 * 
	 * @throws WebDavException
	 *             If the resource does not exist, the user has insufficient
	 *             privileges, or the resource could not be deleted.
	 */
	void delete(String uri) throws WebDavException;
	
	/**
	 * Copies a resource.
	 * 
	 * @param uri
	 *            The resource source URI.
	 * @param destination
	 *            The resource destination URI.
	 * @param overwrite
	 *            Whether an existing destination resource should be
	 *            overwritten.
	 * @throws WebDavException
	 *             If the resource does not exist, the user has insufficient
	 *             privileges, or the resource could not be copied.
	 */
	void copy(String uri, String destination, boolean overwrite) throws WebDavException;
	
    /**
     * Moves a resource.
     * 
     * @param uri
     *            The resource source URI.
     * @param destination
     *            The resource destination URI.
	 * @param overwrite
	 *            Whether an existing destination resource should be
	 *            overwritten.
     * 
     * @throws WebDavException
     *             If the resource does not exist, the user has insufficient
     *             privileges, or the resource could not be moved.
     */
    void move(String uri, String destination, boolean overwrite) throws WebDavException;
    
}
