package org.ozsoft.xmlindexer;

import java.io.File;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Indexer for XML documents. <br />
 * 
 * Uses XPath-like index paths like "/Document/Header/Id" and "//author". <br />
 * 
 * Supports indexed elements only (no attributes). <br />
 * 
 * Implemented using JAXP SAX.
 * 
 * @author Oscar Stigter
 */
public class XmlIndexer {

    /** JAXP SAX parser. */
    private final SAXParser parser;

    /** SAX content handler. */
    private final IndexHandler handler = new IndexHandler();

    /**
     * Constructs an XmlIndexer.
     */
    public XmlIndexer() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        factory.setXIncludeAware(false);
        try {
            parser = factory.newSAXParser();
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating SAX parser: " + e.getMessage());
        }
    }

    /**
     * Indexes an XML document.
     * 
     * @param file
     *            The XML document.
     * @param indices
     *            The indices.
     */
    public void index(File file, Index[] indices) {
        handler.setIndices(indices);
        try {
            long startTime = System.currentTimeMillis();
            parser.parse(file, handler);
            long duration = System.currentTimeMillis() - startTime;
            System.out.format("Document indexed in %d ms\n", duration);
        } catch (IOException e) {
            System.err.println("ERROR: Could not read file: " + e.getMessage());
        } catch (SAXException e) {
            System.err.println("ERROR: Could not parse XML file: " + e.getMessage());
        }
    }

    /**
     * SAX content handler collecting the index values.
     * 
     * @author Oscar Stigter
     */
    private static class IndexHandler extends DefaultHandler {

        /** Stack with hierarchival XML elements, forming the path. */
        private final Deque<String> elements;

        /** Element text buffer. */
        private final StringBuilder sb;

        /** The indices. */
        private Index[] indices;

        /**
         * Constructs the SAX content handler.
         */
        public IndexHandler() {
            elements = new LinkedList<String>();
            sb = new StringBuilder();
        }

        /**
         * Sets the indices.
         * 
         * @param indices The indices.
         */
        public void setIndices(Index[] indices) {
            this.indices = indices;
        }

        /*
         * (non-Javadoc)
         * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
            elements.add(localName);
            sb.delete(0, sb.length());
        }

        /*
         * (non-Javadoc)
         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            final String path = getCurrentPath();
            for (Index index : indices) {
                if (index.getPath().startsWith("//")) {
                    // Descendant-style path.
                    if (path.endsWith(index.getPath().substring(1))) {
                        final String text = sb.toString().trim();
                        if (text.length() > 0) {
                            index.setValue(text);
                        }
                    }
                } else {
                    // Absolute path.
                    if (path.equals(index.getPath())) {
                        final String text = sb.toString().trim();
                        if (text.length() != 0) {
                            index.setValue(text);
                        }
                    }
                }
            }
            elements.removeLast();
        }

        /*
         * (non-Javadoc)
         * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
         */
        @Override
        public void characters(char[] buffer, int offset, int length) throws SAXException {
            sb.append(buffer, offset, length);
        }

        private String getCurrentPath() {
            final StringBuilder path = new StringBuilder();
            for (String element : elements) {
                path.append('/');
                path.append(element);
            }
            return path.toString();
        }

    }

}
