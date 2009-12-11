package com.atosorigin.nl.dimpact.midoffice.stufxml.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Utility class with generic XML functionality.
 *
 * @author Oscar Stigter
 */
public final class XmlHelper {

	/** Log. */
	private static final Log LOG = LogFactory.getLog(XmlHelper.class);

	/** W3C DOM document builder. */
	private static DocumentBuilder docBuilder;

	static {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			docBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// Should never happen.
			String message = "Could not create DOM DocumentBuilder";
			LOG.fatal(message, e);
		}
	}

	/**
	 * Private constructor to deny instantiation.
	 */
	private XmlHelper() {
		// Empty implementation.
	}

	public static Document createDocument(InputStream is) throws IOException, SAXException {
		return docBuilder.parse(is);
	}

	public static Element getSingleElementByName(Node node, String name) {
		NodeList nodeList = node.getChildNodes();
		int count = nodeList.getLength();
		for (int i = 0; i < count; i++) {
			Node child = nodeList.item(i);
			if (child instanceof Element && child.getNodeName().equals(name)) {
				return (Element) child;
			}
		}
		return null;
	}

	public static List<Element> getMultipleElementsByName(Node node, String name) {
		List<Element> elements = new ArrayList<Element>();
		NodeList nodeList = node.getChildNodes();
		int count = nodeList.getLength();
		for (int i = 0; i < count; i++) {
			Node child = nodeList.item(i);
			if (child instanceof Element && child.getNodeName().equals(name)) {
				elements.add((Element) child);
			}
		}
		return elements;
	}

	public static List<Node> selectMultipleNodes(String expression,
			InputStream is, NamespaceContext namespaceContext)
			throws IOException, SAXException, XPathException {
		Node node = createDocument(is);
		return selectMultipleNodes(expression, node, namespaceContext);
	}

	public static List<Node> selectMultipleNodes(String expression,
			Node node, NamespaceContext namespaceContext) throws XPathException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(namespaceContext);
		NodeList nodeList = (NodeList) xpath.evaluate(expression, node, XPathConstants.NODESET);
		List<Node> nodes = new ArrayList<Node>();
		int nodeCount = nodeList.getLength();
		for (int i = 0; i < nodeCount; i++) {
			nodes.add(nodeList.item(i));
		}
		return nodes;
	}

}
