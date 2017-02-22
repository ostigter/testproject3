package org.ozsoft.blackbeard.providers;

import java.util.Set;

import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.util.http.HttpClient;

/**
 * ExtraTorrent search provider.
 * 
 * @author Oscar Stigter
 */
public class ExtraTorrentSearchProvider extends AbstractSearchProvider {

    private static final String URI = "http://extratorrent.cc/rss.xml?type=search&cid=8&search=%s";

    @Override
    public Set<Torrent> search(String text, HttpClient httpClient) {
        String uri = String.format(URI, encodeUrl(text));
        return searchTorrentsFromRssFeed(uri, httpClient);
    }
}
