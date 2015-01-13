package org.ozsoft.blackbeard.providers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.parsers.TorrentRssParser;
import org.xml.sax.SAXException;

public abstract class AbstractSearchProvider implements SearchProvider {

    protected static final int MAX_PAGES = 5;

    private static final String UTF8 = "UTF-8";

    private static final Set<Torrent> EMPTY_SET = new TreeSet<Torrent>();

    @Override
    public abstract Set<Torrent> search(String text);

    protected static Set<Torrent> searchTorrentsFromRssFeed(String uri) {
        // System.out.format("### Searching torrents using URI '%s'\n", uri);
        Set<Torrent> torrents = EMPTY_SET;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                torrents = TorrentRssParser.parse(response.getEntity().getContent());
            } else {
                System.err.format(
                        "ERROR: Could not retrieve RSS feed with search results from URI '%s' (HTTP status: %d)\n",
                        uri, statusCode);
            }
        } catch (IOException e) {
            System.err.format("ERROR: Could not retrieve RSS feed with search results from URI '%s'\n", uri);
            e.printStackTrace();
        } catch (SAXException | ParserConfigurationException e) {
            System.err.format("ERROR: Could not parse RSS feed with search results from URI '%s'\n", uri);
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(response);
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
