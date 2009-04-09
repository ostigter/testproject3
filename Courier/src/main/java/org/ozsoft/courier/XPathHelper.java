package org.ozsoft.courier;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

/**
 * Utility class for evaluating XPath expressions.
 * 
 * @author Oscar Stigter
 */
public class XPathHelper {
    
    /** The log. */
    private static final Logger LOG = Logger.getLogger(XPathHelper.class);
    
    /** The XPath factory. */
	private static final XPathFactory XPATH_FACTORY = XPathFactory.newInstance();
    
    /**
	 * Evaluates an XPath expression against an XML node.
	 * 
	 * @param node
	 *            The XML node.
	 * @param expression
	 *            The XPath expression.
	 * @param nc
	 *            The namespace context.
	 * 
	 * @return The result of the XPath expression.
	 */
	public static String evaluate(Node node, String expression, NamespaceContext nc) {
        XPath xpath = XPATH_FACTORY.newXPath();
        xpath.setNamespaceContext(nc);
        String result = null;
        try {
            result = xpath.evaluate(expression, node);
        } catch (XPathException e) {
            LOG.error("Error evaluating XPath expression", e);
        }
        return result;
    }

}
