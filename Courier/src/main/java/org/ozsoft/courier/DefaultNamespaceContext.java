package org.ozsoft.courier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

import org.apache.log4j.Logger;

/**
 * A simple namespace context implementation.
 * 
 * @author Oscar Stigter
 */
public class DefaultNamespaceContext implements NamespaceContext {
    
	/** The log. */
	private static final Logger LOG =
	        Logger.getLogger(DefaultNamespaceContext.class);
    
    /** The namespaces mapped by prefix. */
	private final Map<String, String> namespaces;
    
	/**
	 * Constructor.
	 */
	public DefaultNamespaceContext() {
        namespaces = new HashMap<String, String>();
    }
	
    public void addNamespace(String prefix, String uri) {
        namespaces.put(prefix, uri);
        LOG.debug(String.format(
                "Added namespace with prefix '%s' and URI '%s'", prefix, uri));
    }

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
                LOG.warn(String.format(
                        "Warning: Could not resolve namespace prefix '%s'", prefix));
                return XMLConstants.NULL_NS_URI;
            }
        }
    }

    public String getPrefix(String uri) {
        for (String prefix : namespaces.keySet()) {
            String namespace = namespaces.get(prefix);
            if (namespace.equals(uri)) {
                return prefix;
            }
        }
        LOG.warn(String.format("Could not resolve namespace URI '%s'", uri));
        return XMLConstants.NULL_NS_URI;
    }

    public Iterator<String> getPrefixes(String namespaceURI) {
        return namespaces.keySet().iterator();
    }

}
