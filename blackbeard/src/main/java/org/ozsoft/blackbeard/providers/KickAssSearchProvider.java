package org.ozsoft.blackbeard.providers;

import java.util.Set;

import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.util.http.HttpClient;

/**
 * Kickass Torrents (KAT) search provider. <br />
 * <br />
 * 
 * Obsolete since KAT has been taken down.
 * 
 * @author Oscar Stigter
 */
public class KickAssSearchProvider extends AbstractSearchProvider {

    private static final String URI = "http://kickass.to/usearch/%s/?rss=1";

    @Override
    public Set<Torrent> search(String text, HttpClient httpClient) {
        String uri = String.format(URI, encodeUrl(text + " category:TV verified:1"));
        return searchTorrentsFromRssFeed(uri, httpClient);
    }
}
