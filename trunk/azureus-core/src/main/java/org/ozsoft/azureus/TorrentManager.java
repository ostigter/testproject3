package org.ozsoft.azureus;

import java.util.Collection;

public interface TorrentManager {

    void start() throws TorrentException;

    void stop() throws TorrentException;

    boolean isStarted();

    Torrent downloadTorrent(String location) throws TorrentException;

    Collection<Torrent> getTorrents() throws TorrentException;

    Torrent getTorrent(String id) throws TorrentException;

    int getGlobalDownloadSpeed() throws TorrentException;

    int getGlobalUploadSpeed() throws TorrentException;

    long getTotalBytesReceived() throws TorrentException;

    long getTotalBytesSent() throws TorrentException;
}
