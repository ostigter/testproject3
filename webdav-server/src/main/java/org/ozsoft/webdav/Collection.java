package org.ozsoft.webdav;

import java.util.LinkedList;
import java.util.List;

/**
 * WebDAV collection resource.
 * 
 * @author Oscar Stigter
 */
public class Collection extends Resource {
    
	/** Child resources. */
	private final List<Resource> resources;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The collection name.
	 */
	public Collection(String name) {
    	super(name);
        resources = new LinkedList<Resource>();
        setProperty(WebDavConstants.RESOURCETYPE, WebDavConstants.COLLECTION);
    }
    
	/**
	 * Returns the child resources.
	 * 
	 * @return The child resources.
	 */
	public List<Resource> getResources() {
        return resources;
    }

	/**
	 * Adds a child resource.
	 * 
	 * @param resource
	 *            The child resource.
	 */
	public void addResource(Resource resource) {
		if (!resources.contains(resource)) {
			resources.add(resource);
		}
    }
    
	/**
	 * Removes a child resource.
	 * 
	 * @param resource
	 *            The child resource.
	 */
	public void removeResource(Resource resource) {
        resources.remove(resource);
    }
    
}
