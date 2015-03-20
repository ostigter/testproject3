package org.ozsoft.azureus;

import java.util.Collection;

public interface TorrentManager {

    void start() throws TorrentException;

    void stop() throws TorrentException;

    boolean isStarted();

    Torrent downloadTorrent(String location) throws TorrentException;

    Collection<Torrent> getTorrents() throws TorrentException;

    Torrent getTorrent(String id) throws TorrentException;

    void startTorrent(Torrent torrent) throws TorrentException;

    void stopTorrent(Torrent torrent) throws TorrentException;

    void removeTorrent(Torrent torrent) throws TorrentException;

    void deleteTorrent(Torrent torrent) throws TorrentException;

    int getDownloadSpeed() throws TorrentException;

    int getUploadSpeed() throws TorrentException;

    long getTotalBytesReceived() throws TorrentException;

    long getTotalBytesSent() throws TorrentException;
}
