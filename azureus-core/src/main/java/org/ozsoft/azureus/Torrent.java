package org.ozsoft.azureus;

import java.io.File;
import java.util.List;

public interface Torrent {

    String getId();

    String getName();

    long getSize();

    List<File> getFiles();

    TorrentStatus getStatus();

    int getPeerCount();

    int getSeedCount();

    long getDownloadSpeed();

    long getUploadSpeed();

    long getBytesReceived();

    long getBytesSent();

    double getProgress();

    long getRemainingTime();

    void start() throws TorrentException;

    void stop() throws TorrentException;

    void remove() throws TorrentException;

    void delete() throws TorrentException;
}
