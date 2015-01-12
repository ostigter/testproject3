package org.ozsoft.blackbeard.providers;

import java.util.HashSet;
import java.util.Set;

import org.ozsoft.blackbeard.domain.Torrent;

public class TorrentzSearchProvider extends AbstractSearchProvider {

    private static final String URI = "http://torrentz.eu/feed?f=%s&p=%d";

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
