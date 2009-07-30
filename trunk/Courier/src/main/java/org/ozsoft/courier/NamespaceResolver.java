package org.ozsoft.courier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.apache.log4j.Logger;

/**
 * A simple namespace resolver.
 * 
 * @author Oscar Stigter
 */
public class NamespaceResolver implements NamespaceContext {
    
	/** The log. */
	private static final Logger LOG = Logger.getLogger(NamespaceResolver.class);
    
    /** The namespaces mapped by prefix. */
	private final Map<String, String> namespaces;
    
	/**
	 * Constructor.
	 */
	public NamespaceResolver() {
        namespaces = new HashMap<String, String>();
    }
	
    /**
	 * Adds a namespace mapping.
	 * 
	 * @param prefix
	 *            The namespace prefix.
	 * @param uri
	 *            The namespace URI.
	 */
	public void addNamespaceMapping(String prefix, String uri) {
		if (prefix == null) {
			throw new IllegalArgumentException("Null prefix");
		}
		if (uri == null || uri.length() == 0) {
			throw new IllegalArgumentException("Null or empty uri");
		}
		if (prefix.length() != 0 && !namespaces.containsKey(prefix)) {
			namespaces.put(prefix, uri);
			LOG.debug(String.format("Mapped namespace prefix '%s' to URI '%s'", prefix, uri));
		}
    }

	/*
	 * (non-Javadoc)
	 * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
	 */
	public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Null prefix");
        }
        if (prefix.length() == 0) {
            return XMLConstants.NULL_NS_URI;
        } else {
            String uri = namespaces.get(prefix);
            if (uri != null) {
                return uri;
            } else {
                LOG.debug(String.format("Could not resolve namespace prefix '%s'", prefix));
                return XMLConstants.NULL_NS_URI;
            }
        }
    }

	/*
	 * (non-Javadoc)
	 * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
	 */
	public String getPrefix(String uri) {
        for (String prefix : namespaces.keySet()) {
            String namespace = namespaces.get(prefix);
            if (namespace.equals(uri)) {
                return prefix;
            }
        }
        LOG.debug(String.format("Could not resolve namespace URI '%s'", uri));
        return XMLConstants.NULL_NS_URI;
    }

    /*
     * (non-Javadoc)
     * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
     */
	public Iterator<String> getPrefixes(String namespaceURI) {
        return namespaces.keySet().iterator();
    }

}
