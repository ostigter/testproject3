package org.ozsoft.azureus;

public interface TorrentListener {

    void downloadCompleted(Torrent torrent);

    void downloadFailed(Torrent torrent);
}
