package org.ozsoft.webdav;

import java.util.ArrayList;
import java.util.List;

/**
 * A WebDAV property response.
 * 
 * Wrapper for a 'response' element as part of a a 'multistatus' element.
 * 
 * @author Oscar Stigter
 */
public class WebDavResponse {
    
    /** The resource URI ('href' element). */
    private final String uri;
    
    /** The property status. */
    private final List<PropStat> propStats;
    
    /**
     * Constructor.
     * 
     * @param uri
     *            The resource URI.
     */
    public WebDavResponse(String uri) {
        this.uri = uri;
        propStats = new ArrayList<PropStat>();
    }
    
    /**
     * Returns the resource URI.
     * 
     * @return The resource URI.
     */
    public String getUri() {
        return uri;
    }
    
    /**
     * Returns the property status.
     * 
     * @return The property status.
     */
    public List<PropStat> getPropStats() {
        return propStats;
    }
    
    /**
     * Adds a property status.
     * 
     * @param propStat
     *            The property status.
     */
    public void addPropStat(PropStat propStat) {
        propStats.add(propStat);
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return uri;
    }
    
}
