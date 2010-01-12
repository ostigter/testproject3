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
    
    /**
     * Properties.
     */
	private final Map<String, PropStat> properties;
    
	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The resource name (displayname).
	 */
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
	
	/**
	 * Returns the property names.
	 * 
	 * @return The property names.
	 */
	public Set<String> getPropertyNames() {
        Set<String> names = new HashSet<String>();
        for (String name : properties.keySet()) {
            names.add(name);
        }
        return names;
    }
    
	/**
	 * Returns a property status.
	 * 
	 * @param name
	 *            The property name.
	 * 
	 * @return The property status.
	 */
	public PropStat getProperty(String name) {
        return properties.get(name);
    }
    
	/**
	 * Sets a property.
	 * 
	 * @param name
	 *            The property name.
	 * @param value
	 *            The property value.
	 */
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
