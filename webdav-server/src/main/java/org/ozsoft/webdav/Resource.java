package org.ozsoft.webdav;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * WebDAV resource.
 * 
 * @author Oscar Stigter
 */
public class Resource {
    
    private final Map<String, PropStat> properties;
    
    public Resource() {
        properties = new HashMap<String, PropStat>();
    }
    
    public Set<String> getPropertyNames() {
        Set<String> names = new HashSet<String>();
        for (String name : properties.keySet()) {
            names.add(name);
        }
        return names;
    }
    
    public PropStat getProperty(String name) {
        return properties.get(name);
    }
    
    public void setProperty(String name, String value) {
        PropStat propStat = properties.get(name);
        if (propStat == null) {
            propStat = new PropStat(name);
            properties.put(name, propStat);
        }
        propStat.setValue(value);
    }

}
