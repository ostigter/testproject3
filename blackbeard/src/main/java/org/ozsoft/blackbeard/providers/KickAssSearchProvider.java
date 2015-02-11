package org.ozsoft.blackbeard.providers;

import java.util.Set;

import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.util.http.HttpClient;

public class KickAssSearchProvider extends AbstractSearchProvider {

    // private static final String URI = "http://kickass.so/search/%s/?rss=1";
    private static final String URI = "https://kickass.unblocked.pw/search/%s/?rss=1";

    @Override
    public Set<Torrent> search(String text, HttpClient httpClient) {
        String uri = String.format(URI, encodeUrl(text + " category:TV verified:1"));
        return searchTorrentsFromRssFeed(uri, httpClient);
    }
}
