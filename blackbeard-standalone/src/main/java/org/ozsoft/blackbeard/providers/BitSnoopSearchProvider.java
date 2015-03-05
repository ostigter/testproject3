package org.ozsoft.blackbeard.providers;

import java.util.Set;
import java.util.TreeSet;

import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.util.http.HttpClient;

public class BitSnoopSearchProvider extends AbstractSearchProvider {

    private static final String URI = "http://bitsnoop.com/search/video/%s/c/d/%d/?fmt=rss";

    @Override
    public Set<Torrent> search(String text, HttpClient httpClient) {
        Set<Torrent> torrents = new TreeSet<Torrent>();

        int page = 1;
        boolean hasNew = true;

        while (hasNew && page <= MAX_PAGES) {
            String uri = String.format(URI, encodeUrl(text), page++);
            hasNew = torrents.addAll(searchTorrentsFromRssFeed(uri, httpClient));
        }

        return torrents;
    }
}
