package org.ozsoft.blackbeard.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.ozsoft.blackbeard.domain.Episode;
import org.ozsoft.blackbeard.domain.Show;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser for TV show episode lists from TVRage.
 * 
 * @author Oscar Stigter
 */
public class EpisodeListParser {

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

    public static List<Episode> parse(String text) throws ParserConfigurationException, SAXException, IOException {
        SAXParser parser = SAX_PARSER_FACTORY.newSAXParser();
        EpisodeListHandler handler = new EpisodeListHandler();
        parser.parse(new InputSource(IOUtils.toInputStream(text)), handler);
        return handler.getEpisodes();
    }

    /**
     * SAX content handler for an episode lists for a specific TV show from TVRage. <br />
     * <br />
     * 
     * After parsing the list, the {@link getEpisodes} method can be used to retrieve the episodes. <br />
     * <br />
     * 
     * @author Oscar Stigter
     */
    private static class EpisodeListHandler extends DefaultHandler {

        private final List<Episode> episodes;

        private String nodePath = "";

        private final StringBuilder text = new StringBuilder();

        private int episodeId;

        private int seasonNumber;

        private int episodeNumber;

        private String title;

        private Date airDate;

        private String link;

        /**
         * Constructor.
         */
        public EpisodeListHandler() {
            episodes = new ArrayList<Episode>();
        }

        /**
         * Returns the episodes specified in the XML document.
         * 
         * @return The {@link Show}s.
         */
        public List<Episode> getEpisodes() {
            return episodes;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            nodePath = String.format("%s/%s", nodePath, localName);
            text.setLength(0);

            if (nodePath.equals("/Show/Episodelist/Season")) {
                seasonNumber = Integer.parseInt(attributes.getValue("no"));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            // System.out.format("### endElement: '%s', text = '%s'\n", localName, text.toString());
            if (nodePath.equals("/Show/Episodelist/Season/episode/epnum")) {
                episodeId = Integer.parseInt(text.toString());
            } else if (nodePath.equals("/Show/Episodelist/Season/episode/seasonnum")) {
                episodeNumber = Integer.parseInt(text.toString());
            } else if (nodePath.equals("/Show/Episodelist/Season/episode/title")) {
                title = text.toString();
            } else if (nodePath.equals("/Show/Episodelist/Season/episode/airdate")) {
                // airdate =
            } else if (nodePath.equals("/Show/Episodelist/Season/episode/link")) {
                link = text.toString();
            } else if (nodePath.equals("/Show/Episodelist/Season/episode")) {
                episodes.add(new Episode(episodeId, seasonNumber, episodeNumber, title, airDate, link));
            }
            nodePath = nodePath.substring(0, nodePath.lastIndexOf('/'));
        }

        @Override
        public void characters(char[] buffer, int start, int length) {
            text.append(String.copyValueOf(buffer, start, length));
        }
    }
}
