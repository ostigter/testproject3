package org.ozsoft.webdav.server;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.ozsoft.webdav.Collection;
import org.ozsoft.webdav.PropStat;
import org.ozsoft.webdav.Resource;
import org.ozsoft.webdav.WebDavConstants;
import org.ozsoft.webdav.WebDavException;
import org.ozsoft.webdav.WebDavStatus;

/**
 * In-memory backend for a WebDAV server.
 * 
 * Useful for testing and demonstration purposes.
 * 
 * @author Oscar Stigter
 */
public class MemoryBackend implements WebDavBackend {
    
    /** Log. */
    private static final Logger LOG = Logger.getLogger(MemoryBackend.class);
    
    /** Resources mapped by URI. */
    private final Map<String, Resource> resources;
    
    /**
     * Constructor.
     */
    public MemoryBackend() {
        resources = new HashMap<String, Resource>();
        Collection rootCol = new Collection("/");
        resources.put("/", rootCol);
        Resource res = new Resource("README.txt");
        rootCol.addResource(res);
        resources.put("/README.txt", res);
        Collection dataCol = new Collection("data");
        rootCol.addResource(dataCol);
        resources.put("/data", dataCol);
        res = new Resource("001.xml");
        dataCol.addResource(res);
        resources.put("/data/001.xml", res);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#exists(java.lang.String)
     */
    @Override
    public boolean exists(String uri) {
        return resources.containsKey(uri);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#isCollection(java.lang.String)
     */
    @Override
    public boolean isCollection(String uri) {
        Resource res = resources.get(uri);
        if (uri == null) {
            return false;
        } else {
            return (res instanceof Collection);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#createCollection(java.lang.String)
     */
    @Override
    public void createCollection(String uri) throws WebDavException {
        int pos = uri.lastIndexOf('/');
        if (pos == -1) {
            throw new IllegalArgumentException("Invalid URI");
        }
        String parentUri = uri.substring(0, pos);
        if (!isCollection(parentUri)) {
            throwWebDavException(HttpServletResponse.SC_CONFLICT,
                    String.format("Parent collection '%s' does not exist", parentUri));
        }
        String name = uri.substring(pos + 1);
        Collection parentCol = (Collection) resources.get(parentUri);
        Collection col = new Collection(name);
        parentCol.addResource(col);
        resources.put(uri, col);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#createResource(java.lang.String)
     */
    @Override
    public void createResource(String uri) throws WebDavException {
        int pos = uri.lastIndexOf('/');
        if (pos == -1) {
            throw new IllegalArgumentException("Invalid URI");
        }
        String parentUri = uri.substring(0, pos);
        if (!isCollection(parentUri)) {
            throwWebDavException(HttpServletResponse.SC_CONFLICT,
                    String.format("Parent collection '%s' does not exist", parentUri));
        }
        Collection parentCol = (Collection) resources.get(parentUri);
        String name = uri.substring(pos + 1);
        Collection col = new Collection(name);
        resources.put(uri, col);
        parentCol.addResource(col);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#listCollection(java.lang.String)
     */
    @Override
    public List<String> listCollection(String uri) throws WebDavException {
        if (!isCollection(uri)) {
            throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
                    String.format("Collection '%s' does not exist", uri));
        }
        Collection col = (Collection) resources.get(uri);
        List<String> names = new ArrayList<String>();
        for (Resource resource : col.getResources()) {
        	PropStat propStat = resource.getProperty(WebDavConstants.DISPLAYNAME);
        	if (propStat != null) {
        		names.add(propStat.getValue());
        	}
        }
        return names;
    }
    
	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getDisplayName(java.lang.String)
	 */
    @Override
	public String getDisplayName(String uri) throws WebDavException {
    	PropStat propStat = getPropStat(uri, WebDavConstants.DISPLAYNAME);
    	if (propStat == null) {
            throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    String.format("No 'displayname' property for resource '%s'", uri));
    	}
		return propStat.getValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getResourceType(java.lang.String)
	 */
    @Override
	public String getResourceType(String uri) throws WebDavException {
    	PropStat propStat = getPropStat(uri, WebDavConstants.RESOURCETYPE);
    	if (propStat == null) {
            throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    String.format("No 'resourcetype' property for resource '%s'", uri));
    	}
		return propStat.getValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getCreated(java.lang.String)
	 */
    @Override
	public String getCreated(String uri) throws WebDavException {
    	PropStat propStat = getPropStat(uri, WebDavConstants.CREATIONDATE);
    	if (propStat == null) {
            throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    String.format("No 'creationdate' property for resource '%s'", uri));
    	}
		return propStat.getValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getLastModified(java.lang.String)
	 */
    @Override
	public String getLastModified(String uri) throws WebDavException {
    	PropStat propStat = getPropStat(uri, WebDavConstants.GETLASTMODIFIED);
    	if (propStat == null) {
            throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    String.format("No 'getlastmodified' property for resource '%s'", uri));
    	}
		return propStat.getValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.ozsoft.webdav.server.WebDavBackend#getContentType(java.lang.String)
	 */
    @Override
	public String getContentType(String uri) throws WebDavException {
    	PropStat propStat = getPropStat(uri, WebDavConstants.GETCONTENTTYPE);
    	if (propStat == null) {
            throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    String.format("No 'getcontenttype' property for resource '%s'", uri));
    	}
		return propStat.getValue();
	}

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#getContentLength(java.lang.String)
     */
    @Override
	public String getContentLength(String uri) throws WebDavException {
    	PropStat propStat = getPropStat(uri, WebDavConstants.GETCONTENTLENGTH);
    	if (propStat == null) {
            throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    String.format("No 'getcontentlength' property for resource '%s'", uri));
    	}
		return propStat.getValue();
	}

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#getPropStat(java.lang.String, java.lang.String)
     */
    @Override
    public PropStat getPropStat(String uri, String name) throws WebDavException {
        if (!exists(uri)) {
            throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
                    String.format("Resource '%s' not found", uri));
        }
        Resource res = resources.get(uri);
    	PropStat propStat = res.getProperty(name);
    	if (propStat == null) {
    		propStat = new PropStat(name);
    		propStat.setStatus(WebDavStatus.NOT_FOUND);
    	}
    	return propStat;
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#setProperty(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void setProperty(String uri, String name, String value) throws WebDavException {
        if (!exists(uri)) {
            throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
                    String.format("Resource '%s' not found", uri));
        }
        Resource res = resources.get(uri);
    	res.setProperty(name, value);
    }
    
    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#getContent(java.lang.String)
     */
    @Override
    public InputStream getContent(String uri) throws WebDavException {
    	// Retrieving resource content not supported.
        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#setContent(java.lang.String, java.io.InputStream, java.lang.String, java.lang.String)
     */
    @Override
    public void setContent(String uri, InputStream content, String contentType,
            String encoding) throws WebDavException {
    	// Setting resource content not supported.
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#delete(java.lang.String)
     */
    @Override
    public void delete(String uri) throws WebDavException {
    	//TODO: Delete resource.
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#copy(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void copy(String uri, String destination, boolean overwrite) throws WebDavException {
    	//TODO: Copy resource.
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#move(java.lang.String, java.lang.String, boolean)
     */
    @Override
    public void move(String uri, String destination, boolean overwrite) throws WebDavException {
    	//TODO: Move resource.
    }

    /**
     * Throws a WebDAV exception with a nested exception as the cause.
     * 
     * @param statusCode
     *            The HTTP status code.
     * @param message
     *            The message.
     * 
     * @throws WebDavException
     *             The exception.
     */
    private void throwWebDavException(int statusCode, String message) throws WebDavException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
        throw new WebDavException(statusCode, message);
    }

}
