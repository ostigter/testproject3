package org.ozsoft.webdav;

import java.util.Date;
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
    
    public Resource(String name) {
        properties = new HashMap<String, PropStat>();
        setProperty(WebDavConstants.DISPLAYNAME, name);
        setProperty(WebDavConstants.RESOURCETYPE, WebDavConstants.RESOURCE);
        Date date = new Date();
        String created = WebDavConstants.CREATION_DATE_FORMAT.format(date);
        setProperty(WebDavConstants.CREATIONDATE, created);
        String modified = WebDavConstants.LASTMODIFIED_DATE_FORMAT.format(date);
        setProperty(WebDavConstants.GETLASTMODIFIED, modified);
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
            propStat.setStatus(WebDavStatus.OK);
            properties.put(name, propStat);
        }
        propStat.setValue(value);
    }

}
