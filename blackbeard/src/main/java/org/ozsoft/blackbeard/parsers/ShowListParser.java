package org.ozsoft.blackbeard.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.ozsoft.blackbeard.domain.Show;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser for show lists from TVRage.
 * 
 * @author Oscar Stigter
 */
public class ShowListParser {

    private static final SAXParserFactory SAX_PARSER_FACTORY;

    static {
        SAX_PARSER_FACTORY = SAXParserFactory.newInstance();
        SAX_PARSER_FACTORY.setNamespaceAware(true);
        SAX_PARSER_FACTORY.setValidating(false);
        SAX_PARSER_FACTORY.setXIncludeAware(false);
        try {
            SAX_PARSER_FACTORY.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (Exception e) {
            System.err.println("Could not create SAXParserFactory");
            e.printStackTrace(System.err);
        }
    }

    public static List<Show> parse(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        SAXParser parser = SAX_PARSER_FACTORY.newSAXParser();
        ShowListHandler handler = new ShowListHandler();
        parser.parse(new InputSource(is), handler);
        return handler.getShows();
    }

    /**
     * SAX content handler for show lists. <br />
     * <br />
     * 
     * After parsing the show list, the {@link getShows} method can be used to retrieve the shows. <br />
     * <br />
     * 
     * @author Oscar Stigter
     */
    private static class ShowListHandler extends DefaultHandler {

        private final List<Show> shows;

        private String nodePath = "";

        private final StringBuilder text = new StringBuilder();

        private Show show;

        /**
         * Constructor.
         */
        public ShowListHandler() {
            shows = new ArrayList<Show>();
        }

        /**
         * Returns the shows specified in the RSS feed.
         * 
         * @return The {@link Show}s.
         */
        public List<Show> getShows() {
            return shows;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            nodePath = String.format("%s/%s", nodePath, localName);
            text.setLength(0);

            if (nodePath.equals("/Results/show")) {
                // Start of show.
                show = new Show();
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (nodePath.equals("/Results/show/showid")) {
                show.id = Integer.parseInt(text.toString());
            } else if (nodePath.equals("/Results/show/name")) {
                show.name = text.toString();
            } else if (nodePath.equals("/Results/show")) {
                shows.add(show);
            }
            nodePath = nodePath.substring(0, nodePath.lastIndexOf('/'));
        }

        @Override
        public void characters(char[] buffer, int start, int length) {
            text.append(String.copyValueOf(buffer, start, length));
        }
    }
}
