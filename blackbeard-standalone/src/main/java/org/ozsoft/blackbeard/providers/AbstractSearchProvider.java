package org.ozsoft.blackbeard.providers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpStatus;
import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.parsers.TorrentRssParser;
import org.ozsoft.blackbeard.util.http.HttpClient;
import org.ozsoft.blackbeard.util.http.HttpResponse;
import org.xml.sax.SAXException;

public abstract class AbstractSearchProvider implements SearchProvider {

    protected static final int MAX_PAGES = 5;

    private static final String UTF8 = "UTF-8";

    private static final Set<Torrent> EMPTY_SET = new TreeSet<Torrent>();

    @Override
    public abstract Set<Torrent> search(String text, HttpClient httpClient);

    protected static Set<Torrent> searchTorrentsFromRssFeed(String uri, HttpClient httpClient) {
        // System.out.format("### Searching torrents using URI '%s'\n", uri);
        Set<Torrent> torrents = EMPTY_SET;
        try {
            HttpResponse httpResponse = httpClient.executeGet(uri);
            int statusCode = httpResponse.getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                torrents = TorrentRssParser.parse(httpResponse.getBody());
            } else {
                System.err.format("ERROR: Could not retrieve RSS feed with search results from URI '%s' (HTTP status: %d)\n", uri, statusCode);
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            System.err.format("ERROR: Could not parse RSS feed with search results from URI '%s'\n", uri);
            e.printStackTrace();
        }

        return torrents;
    }

    protected static String encodeUrl(String uri) {
        try {
            return URLEncoder.encode(uri, UTF8);
        } catch (UnsupportedEncodingException e) {
            // Can never happen since UTF-8 is supported by default.
            throw new RuntimeException(e);
        }
    }
}
