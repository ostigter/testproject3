package org.ozsoft.xmldb;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Collection extends Resource {
    
    private final Map<String, Resource> resources;
    
    /* package */ Collection(Database database) {
        super(database);
        resources = new TreeMap<String, Resource>();
    }
    
    public List<Resource> getResources() {
        List<Resource> list = new LinkedList<Resource>();
        for (Resource resource : resources.values()) {
            list.add(resource);
        }
        return list;
    }
    
    public Resource getResource(String name) {
        return resources.get(name);
    }
    
    @Override
    public String getText() {
        return name;
    }

    @Override
    public String toXml() {
        return String.format("<collection name=\"%s\" />", name);
    }

    @Override
    public String toString() {
        return String.format("Collection(%s)", name);
    }
    
    /* package */ void addResource(Resource resource) throws XmldbException {
        if (resource == null) {
            throw new IllegalArgumentException("Null resource");
        }

        String name = resource.getName();
        if (getResource(name) != null) {
            throw new XmldbException(String.format("Resource with name '%s' already exists", name));
        }
        
        resources.put(name, resource);
    }

    @Override
    /* package */ String toXml(int indent) {
        return toXml();
    }
    
}
