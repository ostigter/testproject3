package org.ozsoft.blackbeard.providers;

import java.util.Set;

import org.ozsoft.blackbeard.domain.Torrent;

public interface SearchProvider {

    Set<Torrent> search(String uri);
}
