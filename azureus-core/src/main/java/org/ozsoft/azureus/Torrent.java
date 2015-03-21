package org.ozsoft.azureus;

import java.io.File;

public interface Torrent {

    String getId();

    String getTitle();

    String getMagnetUri();

    long getSize();

    File[] getFiles();

    TorrentStatus getStatus();

    void start() throws TorrentException;

    void stop() throws TorrentException;

    void remove() throws TorrentException;

    void delete() throws TorrentException;

    int getSeedCount();

    int getPeerCount();

    int getDownloadSpeed();

    int getUploadSpeed();

    int getBytesReceived();

    int getBytesSent();

    double getProgress();

    int getRemainingTime();
}
