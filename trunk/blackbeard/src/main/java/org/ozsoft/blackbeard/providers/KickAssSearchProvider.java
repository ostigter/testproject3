package org.ozsoft.blackbeard.providers;

import java.util.Set;

import org.ozsoft.blackbeard.domain.Torrent;

public class KickAssSearchProvider extends AbstractSearchProvider {

    private static final String URI = "http://kickass.so/search/%s/?rss=1";

    @Override
    public Set<Torrent> search(String text) {
        String uri = String.format(URI, encodeUrl(text + " category:TV verified:1"));
        return searchTorrentsFromRssFeed(uri);
    }
}
