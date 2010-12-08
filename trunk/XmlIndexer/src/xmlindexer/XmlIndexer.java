package xmlindexer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Indexer for XML documents using SAX and XPath-like paths.
 * 
 * @author Oscar Stigter
 */
public class XmlIndexer {

    private final SAXParser parser;

    private final XPathHandler handler = new XPathHandler();

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

    public void index(File file, Index[] indices) {
        handler.setIndices(indices);
        try {
            parser.parse(file, handler);
        } catch (IOException e) {
            System.err.println(e);
        } catch (SAXException e) {
            System.err.println(e);
        }
    }

    // ------------------------------------------------------------------------
    // Inner classes
    // ------------------------------------------------------------------------

    private class XPathHandler extends DefaultHandler {

        private final List<String> elements;

        private Index[] indices;

        private StringBuilder sb;

        public XPathHandler() {
            elements = new ArrayList<String>();
        }

        public void setIndices(Index[] indices) {
            this.indices = indices;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
            System.out.println("Start Element: " + localName);
            elements.add(localName);
            sb = new StringBuilder();
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            System.out.println("End Element:   " + localName);
            String path = getCurrentPath();
            System.out.println("Path: " + path);
            for (Index index : indices) {
                System.out.println("Index path: " + index.getPath());
                boolean matches = false;
                if (index.getPath().startsWith("//")) {
                    if (path.endsWith(index.getPath())) {
                        String text = sb.toString().trim();
                        if (text.length() != 0) {
                            index.setValue(text);
                        }
                    }
                } else {
                    if (path.equals(index.getPath())) {
                        String text = sb.toString().trim();
                        if (text.length() != 0) {
                            index.setValue(text);
                        }
                    }
                }
                if (matches) {

                }
            }
            elements.remove(elements.size() - 1);
            sb = new StringBuilder();
        }

        @Override
        public void characters(char[] buffer, int offset, int length) throws SAXException {
            sb.append(buffer, offset, length);
        }

        private String getCurrentPath() {
            StringBuilder sb2 = new StringBuilder();
            for (String element : elements) {
                sb2.append('/');
                sb2.append(element);
            }
            return sb2.toString();
        }

    }

}
