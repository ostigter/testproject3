package org.example.dom.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;

/**
 * Simple, generic namespace context.
 *
 * @author Oscar Stigter
 */
public class SimpleNamespaceContext implements NamespaceContext {

	/** Namespace URI's mapped by prefix. */
	private final Map<String, String> namespaces;

	/**
	 * Constructor.
	 */
	public SimpleNamespaceContext() {
		namespaces = new HashMap<String, String>();
	}

	/**
	 * Maps a namespace URI by a prefix.
	 *
	 * @param prefix
	 *            The namespace prefix.
	 * @param uri
	 *            The namespace URI.
	 */
	public void addNamespaceMapping(String prefix, String uri) {
		namespaces.put(prefix, uri);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String)
	 */
	@Override
	public String getNamespaceURI(String prefix) {
		return namespaces.get(prefix);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
	 */
	@Override
	public String getPrefix(String uri) {
		for (Map.Entry<String, String> entry : namespaces.entrySet()) {
			if (entry.getValue().equals(uri)) {
				return entry.getKey();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
	 */
	@Override
	public Iterator<String> getPrefixes(String uri) {
		Set<String> prefixes = new HashSet<String>();
		if (namespaces.containsValue(uri)) {
			for (Map.Entry<String, String> entry : namespaces.entrySet()) {
				if (entry.getValue().equals(uri)) {
					prefixes.add(entry.getKey());
				}
			}
		}
		return prefixes.iterator();
	}

}
