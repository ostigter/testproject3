package org.ozsoft.azureus;

import java.io.File;
import java.util.UUID;

public class TorrentImpl implements Torrent {

    private final String id;

    private final String title;

    private final String magnetUri;

    /* package */TorrentImpl(String title, String magnetUri) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.magnetUri = magnetUri;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getMagnetUri() {
        return magnetUri;
    }

    @Override
    public void start() throws TorrentException {
    }

    @Override
    public void stop() throws TorrentException {
    }

    @Override
    public void remove() throws TorrentException {
    }

    @Override
    public void delete() throws TorrentException {
    }

    @Override
    public int getDownloadSpeed() {
        return 0;
    }

    @Override
    public int getUploadSpeed() {
        return 0;
    }

    @Override
    public long getSize() {
        return 0L;
    }

    @Override
    public File[] getFiles() {
        return null;
    }

    @Override
    public TorrentStatus getStatus() {
        return TorrentStatus.STOPPED;
    }

    @Override
    public int getSeedCount() {
        return 0;
    }

    @Override
    public int getPeerCount() {
        return 0;
    }

    @Override
    public int getBytesReceived() {
        return 0;
    }

    @Override
    public int getBytesSent() {
        return 0;
    }

    @Override
    public double getProgress() {
        return 0;
    }

    @Override
    public int getRemainingTime() {
        return 0;
    }
}
