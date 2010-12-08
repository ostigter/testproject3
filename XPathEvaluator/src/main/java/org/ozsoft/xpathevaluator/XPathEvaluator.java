package org.ozsoft.xpathevaluator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Console application for evaluating XPath expressions with JAXP.
 * 
 * An optional input document may be set. The input XML document must be located
 * in the project directory.
 * 
 * Namespaces mappings are automatically extracted from the input document so
 * they can be used in the XPath expression.
 * 
 * @author Oscar Stigter
 */
public class XPathEvaluator {

    /** The log. */
    private static final Logger LOG = Logger.getLogger(XPathEvaluator.class);

    /** The namespace resolver. */
    private final NamespaceResolver namespaceResolver;

    /** The input XML document. */
    private File documentFile;

    /** The compiled XPath expression. */
    private XPath xpath;

    /**
     * Constructor.
     */
    public XPathEvaluator() {
        namespaceResolver = new NamespaceResolver();
        XPathFactory xpf = XPathFactory.newInstance();
        xpath = xpf.newXPath();
        xpath.setNamespaceContext(namespaceResolver);
    }

    /**
     * Sets the input XML document as file path.
     * 
     * @param path
     *            The document path.
     */
    public void setDocument(String path) {
        if (path == null || path.length() == 0) {
            throw new IllegalArgumentException("Null or empty path");
        }
        setDocument(new File(path));
    }

    /**
     * Sets the input XML document as file.
     * 
     * @param file
     *            The document file.
     */
    public void setDocument(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Null file");
        }

        if (!file.isFile()) {
            String msg = "Input document not found: " + file;
            LOG.error(msg);
            throw new IllegalStateException(msg);
        }

        documentFile = file;
        LOG.debug(String.format("Input document set to '%s'", documentFile));
        retrieveNamespaces();
    }

    /**
     * Evaluates an XPath expression.
     * 
     * @param expression
     *            The XPath expression.
     * 
     * @return The result.
     */
    public String evaluate(String expression) {
        if (expression == null || expression.length() == 0) {
            throw new IllegalArgumentException("Null or empty expression");
        }

        LOG.debug(String.format("Evaluating expression '%s'", expression));
        String result = null;
        try {
            XPathExpression xpe = xpath.compile(expression);
            InputSource source = new InputSource(new FileInputStream(documentFile));
            result = xpe.evaluate(source);
            if (result.length() == 0) {
                result = "(Empty result)";
            }
            LOG.debug("Result:\n" + result);
        } catch (IOException e) {
            LOG.error("ERROR: Could not read input document: " + e.getMessage(), e);
        } catch (XPathException e) {
            LOG.error("ERROR: Could not evaluate XPath expression: " + e.getMessage(), e);
        }
        return result;
    }

    /**
     * Retrieves the namespace declarations from the input document and stores
     * them in the namespace resolver.
     */
    private void retrieveNamespaces() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        factory.setXIncludeAware(false);
        SAXParser parser = null;
        try {
            parser = factory.newSAXParser();
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating SAX parser: " + e.getMessage());
        }
        try {
            parser.parse(documentFile, new NamespaceHandler(namespaceResolver));
        } catch (IOException e) {
            System.err.println(e);
        } catch (SAXException e) {
            System.err.println(e);
        }
    }

    /**
     * Simple namespace resolver.
     * 
     * @author Oscar Stigter
     */
    private static class NamespaceResolver implements NamespaceContext {

        /** The namespaces. */
        private final Map<String, String> namespaces;

        /**
         * Constructor
         */
        public NamespaceResolver() {
            namespaces = new HashMap<String, String>();
        }

        /**
         * Adds a namespace.
         * 
         * @param prefix
         *            The prefix.
         * @param uri
         *            The URI.
         */
        public void addNamespace(String prefix, String uri) {
            namespaces.put(prefix, uri);
            LOG.debug(String.format("Added namespace prefix '%s' for URI '%s'", prefix, uri));
        }

        /**
         * Clears all namespaces.
         */
        public void clear() {
            namespaces.clear();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.xml.namespace.NamespaceContext#getNamespaceURI(java.lang.String
         * )
         */
        @Override
        public String getNamespaceURI(String prefix) {
            return namespaces.get(prefix);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.xml.namespace.NamespaceContext#getPrefix(java.lang.String)
         */
        @Override
        public String getPrefix(String uri) {
            // Not implemented.
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * javax.xml.namespace.NamespaceContext#getPrefixes(java.lang.String)
         */
        @Override
        public Iterator<String> getPrefixes(String uri) {
            // Not implemented.
            return null;
        }

    } // NamespaceResolver

    /**
     * SAX handler to retrieve the namespaces declarations from an XML document
     * and store these in a namespace resolver.
     * 
     * @author Oscar Stigter
     */
    private static class NamespaceHandler extends DefaultHandler {

        /** The namespace resolver. */
        private final NamespaceResolver namespaceResolver;

        /**
         * Constructor.
         * 
         * @param namespaceResolver
         *            The namespace resolver.
         */
        public NamespaceHandler(NamespaceResolver namespaceResolver) {
            this.namespaceResolver = namespaceResolver;
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * org.xml.sax.helpers.DefaultHandler#startPrefixMapping(java.lang.String
         * , java.lang.String)
         */
        @Override
        public void startPrefixMapping(String prefix, String uri) {
            namespaceResolver.addNamespace(prefix, uri);
        }

    } // NamespaceHandler

}
