package org.ozsoft.webdav.server;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
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

/**
 * In-memory WebDAV backend.
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
        Collection rootCol = new Collection();
        resources.put("/", rootCol);
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
        Collection parentCol = (Collection) resources.get(parentUri);
        Collection col = new Collection();
        String name = uri.substring(pos + 1);
        col.setProperty(WebDavConstants.DISPLAYNAME, name);
        col.setProperty(WebDavConstants.RESOURCETYPE, WebDavConstants.COLLECTION);
        Date date = new Date();
        String created = WebDavConstants.CREATION_DATE_FORMAT.format(date);
        col.setProperty(WebDavConstants.CREATIONDATE, created);
        String modified = WebDavConstants.LASTMODIFIED_DATE_FORMAT.format(date);
        col.setProperty(WebDavConstants.GETLASTMODIFIED, modified);
        resources.put(uri, col);
        parentCol.addResource(col);
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
        Collection col = new Collection();
        String name = uri.substring(pos + 1);
        col.setProperty(WebDavConstants.DISPLAYNAME, name);
        col.setProperty(WebDavConstants.RESOURCETYPE, WebDavConstants.RESOURCE);
        Date date = new Date();
        String created = WebDavConstants.CREATION_DATE_FORMAT.format(date);
        col.setProperty(WebDavConstants.CREATIONDATE, created);
        String modified = WebDavConstants.LASTMODIFIED_DATE_FORMAT.format(date);
        col.setProperty(WebDavConstants.GETLASTMODIFIED, modified);
        resources.put(uri, col);
        parentCol.addResource(col);
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#getChildrenNames(java.lang.String)
     */
    @Override
    public String[] getChildrenNames(String uri) throws WebDavException {
        if (!isCollection(uri)) {
            throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
                    String.format("Collection '%s' does not exist", uri));
        }
        Collection col = (Collection) resources.get(uri);
        List<Resource> children = col.getResources();
        String[] names = new String[children.size()];
        int i = 0;
        for (Resource child : children) {
            PropStat propStat = child.getProperty(WebDavConstants.DISPLAYNAME);
            if (propStat != null) {
                names[i++] = propStat.getValue();
            }
        }
        return names;
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#getContentLength(java.lang.String)
     */
    @Override
    public long getContentLength(String uri) throws WebDavException {
        if (!exists(uri)) {
            throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
                    String.format("Resource '%s' not found", uri));
        }
        Resource res = resources.get(uri);
        PropStat propStat = res.getProperty(WebDavConstants.GETCONTENTLENGTH);
        long contentLength = 0L;
        if (propStat != null) {
            contentLength = Long.parseLong(propStat.getValue());
        }
        return contentLength;
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#getContentType(java.lang.String)
     */
    @Override
    public String getContentType(String uri) throws WebDavException {
        if (!exists(uri)) {
            throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
                    String.format("Resource '%s' not found", uri));
        }
        Resource res = resources.get(uri);
        PropStat propStat = res.getProperty(WebDavConstants.GETCONTENTTYPE);
        String contentType = null;
        if (propStat != null) {
            contentType = propStat.getValue();
        }
        return contentType;
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#getCreated(java.lang.String)
     */
    @Override
    public Date getCreated(String uri) throws WebDavException {
        if (!exists(uri)) {
            throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
                    String.format("Resource '%s' not found", uri));
        }
        Resource res = resources.get(uri);
        PropStat propStat = res.getProperty(WebDavConstants.GETCONTENTTYPE);
        Date created = null;
        if (propStat != null) {
            try {
                created = WebDavConstants.CREATION_DATE_FORMAT.parse(propStat.getValue());
            } catch (ParseException e) {
                throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        String.format("Error parsing creation date for resource '%s'", uri));
            }
        }
        return created;
    }

    /*
     * (non-Javadoc)
     * @see org.ozsoft.webdav.server.WebDavBackend#getModified(java.lang.String)
     */
    @Override
    public Date getModified(String uri) throws WebDavException {
        if (!exists(uri)) {
            throwWebDavException(HttpServletResponse.SC_NOT_FOUND,
                    String.format("Resource '%s' not found", uri));
        }
        Resource res = resources.get(uri);
        PropStat propStat = res.getProperty(WebDavConstants.GETCONTENTTYPE);
        Date modified = null;
        if (propStat != null) {
            try {
                modified = WebDavConstants.LASTMODIFIED_DATE_FORMAT.parse(propStat.getValue());
            } catch (ParseException e) {
                throwWebDavException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        String.format("Error parsing last modification date for resource '%s'", uri));
            }
        }
        return modified;
    }

    @Override
    public InputStream getContent(String uri) throws WebDavException {
        return null;
    }

    @Override
    public void setContent(String uri, InputStream content, String contentType,
            String encoding) throws WebDavException {
    }

    @Override
    public void copy(String uri, String destination, boolean overwrite) throws WebDavException {
    }

    @Override
    public void delete(String uri) throws WebDavException {
    }

    @Override
    public void move(String uri, String destination, boolean overwrite)
            throws WebDavException {
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
