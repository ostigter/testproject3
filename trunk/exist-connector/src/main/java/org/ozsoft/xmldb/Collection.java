package org.ozsoft.xmldb;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection stored in an XML database.
 * 
 * @author Oscar Stigter
 */
public class Collection extends Resource {
    
    /** Resources. */
    private final List<Resource> resources;

    /**
     * Constructor.
     * 
     * @param name
     *            The name.
     */
    public Collection(String name) {
	super(name);
	resources = new ArrayList<Resource>();
    }
    
    /**
     * Returns the resources.
     * 
     * @return The resources.
     */
    public List<Resource> getResources() {
	return resources;
    }
    
    /**
     * Adds a resource.
     * 
     * @param resource
     *            The resource.
     */
    public void addResource(Resource resource) {
	if (!resources.contains(resource)) {
	    resources.add(resource);
	}
    }

}
