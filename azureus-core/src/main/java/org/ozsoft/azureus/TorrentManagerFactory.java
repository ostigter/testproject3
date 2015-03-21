package org.ozsoft.azureus;

public class TorrentManagerFactory {

    private TorrentManagerFactory() {
        // Empty implementation.
    }

    public static TorrentManager createTorrentManager() {
        return new TorrentManagerImpl();
    }
}
