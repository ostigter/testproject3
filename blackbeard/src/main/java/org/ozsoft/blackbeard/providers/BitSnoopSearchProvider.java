package org.ozsoft.blackbeard.providers;

import java.util.HashSet;
import java.util.Set;

import org.ozsoft.blackbeard.domain.Torrent;

public class BitSnoopSearchProvider extends AbstractSearchProvider {

    private static final String URI = "http://bitsnoop.com/search/video/%s/c/d/%d/?fmt=rss";

    @Override
    public Set<Torrent> search(String text) {
        Set<Torrent> torrents = new HashSet<Torrent>();

        int page = 1;
        boolean hasNew = true;

        while (hasNew && page <= MAX_PAGES) {
            String uri = String.format(URI, encodeUrl(text), page++);
            hasNew = torrents.addAll(searchTorrentsFromRssFeed(uri));
        }

        return torrents;
    }
}
