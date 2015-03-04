package org.ozsoft.blackbeard.parsers;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.IOUtils;
import org.ozsoft.blackbeard.domain.Torrent;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX parser for torrent RSS feeds.
 * 
 * @author Oscar Stigter
 */
public class TorrentRssParser {

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

    public static Set<Torrent> parse(String text) throws ParserConfigurationException, SAXException, IOException {
        SAXParser parser = SAX_PARSER_FACTORY.newSAXParser();
        RssHandler handler = new RssHandler();
        parser.parse(new InputSource(IOUtils.toInputStream(text)), handler);
        return handler.getTorrent();
    }

    /**
     * SAX content handler for ADELdirectory documents. <br />
     * <br />
     * 
     * After parsing the ADELdirectory, the {@link getTorrents} method can be used to retrieve the documents. <br />
     * <br />
     * 
     * The documents are sorted based on their last modified time (ascending).
     * 
     * @author Oscar Stigter
     */
    private static class RssHandler extends DefaultHandler {

        private String nodePath = "";
        private final StringBuilder text = new StringBuilder();
        private String title;
        private long size;
        private int seederCount;
        private int leecherCount;
        private String magnetUri;
        private boolean isVerified;
        private final Set<Torrent> torrents;

        /**
         * Constructor.
         */
        public RssHandler() {
            torrents = new TreeSet<Torrent>();
        }

        /**
         * Returns the torrents specified in the RSS feed.
         * 
         * @return The {@link Torrent}s.
         */
        public Set<Torrent> getTorrent() {
            return torrents;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            nodePath = String.format("%s/%s", nodePath, localName);
            text.setLength(0);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            if (nodePath.equals("/rss/channel/item/title")) {
                title = text.toString();
            } else if (nodePath.equals("/rss/channel/item/contentLength") || nodePath.equals("/rss/channel/item/size")) {
                size = Long.parseLong(text.toString());
            } else if (nodePath.equals("/rss/channel/item/seeds") || nodePath.equals("/rss/channel/item/numSeeders")) {
                seederCount = Integer.parseInt(text.toString());
            } else if (nodePath.equals("/rss/channel/item/peers") || nodePath.equals("/rss/channel/item/numLeechers")) {
                leecherCount = Integer.parseInt(text.toString());
            } else if (nodePath.equals("/rss/channel/item/magnetURI") || nodePath.equals("/rss/channel/item/torrent/magnetURI")) {
                magnetUri = text.toString().trim();
            } else if (nodePath.equals("/rss/channel/item/verified") || nodePath.equals("/rss/channel/item/torrent/verified")) {
                isVerified = (text.toString().equals("1"));
            } else if (nodePath.equals("/rss/channel/item")) {
                torrents.add(new Torrent(title, size, seederCount, leecherCount, magnetUri, isVerified));
            }
            nodePath = nodePath.substring(0, nodePath.lastIndexOf('/'));
        }

        @Override
        public void characters(char[] buffer, int start, int length) {
            text.append(String.copyValueOf(buffer, start, length));
        }
    }
}
