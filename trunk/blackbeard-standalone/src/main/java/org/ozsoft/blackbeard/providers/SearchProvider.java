package org.ozsoft.blackbeard.providers;

import java.util.Set;

import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.util.http.HttpClient;

public interface SearchProvider {

    Set<Torrent> search(String uri, HttpClient httpClient);
}
