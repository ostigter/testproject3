package org.ozsoft.webdav;

import java.util.LinkedList;
import java.util.List;

public class Collection extends Resource {
    
    private final List<Resource> resources;

    public Collection(String name) {
    	super(name);
        resources = new LinkedList<Resource>();
        setProperty(WebDavConstants.RESOURCETYPE, WebDavConstants.COLLECTION);
    }
    
    public List<Resource> getResources() {
        return resources;
    }

    public void addResource(Resource resource) {
        resources.add(resource);
    }
    
    public void removeResource(Resource resource) {
        resources.remove(resource);
    }
    
}
