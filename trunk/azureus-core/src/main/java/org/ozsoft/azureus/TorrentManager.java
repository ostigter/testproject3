package org.ozsoft.azureus;

import java.util.Collection;

public interface TorrentManager {

    void start() throws TorrentException;

    void stop() throws TorrentException;

    boolean isStarted();

    void addTorrentListener(TorrentListener listener);

    void removeTorrentListener(TorrentListener listener);

    Torrent downloadTorrent(String location, String savePath) throws TorrentException;

    Collection<Torrent> getTorrents() throws TorrentException;

    boolean hasActiveDownloads();

    int getGlobalDownloadSpeed() throws TorrentException;

    int getGlobalUploadSpeed() throws TorrentException;

    long getTotalBytesReceived() throws TorrentException;

    long getTotalBytesSent() throws TorrentException;
}
