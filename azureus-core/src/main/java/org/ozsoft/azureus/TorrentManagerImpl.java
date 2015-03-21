package org.ozsoft.azureus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TorrentManagerImpl implements TorrentManager {

    private final Map<String, Torrent> torrents;

    private boolean isStarted;

    /* package */TorrentManagerImpl() {
        torrents = new HashMap<String, Torrent>();
        isStarted = false;
    }

    @Override
    public void start() throws TorrentException {
        if (!isStarted) {
            isStarted = true;
            System.out.println("TorrentManager started.");
        }
    }

    @Override
    public void stop() throws TorrentException {
        if (isStarted) {
            isStarted = false;
            System.out.println("TorrentManager stopped.");
        }
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public Torrent downloadTorrent(String location) throws TorrentException {
        verifyIsStarted();
        return null;
    }

    @Override
    public Collection<Torrent> getTorrents() throws TorrentException {
        verifyIsStarted();
        return torrents.values();
    }

    @Override
    public Torrent getTorrent(String id) throws TorrentException {
        verifyIsStarted();
        return torrents.get(id);
    }

    private void verifyIsStarted() throws TorrentException {
        if (!isStarted) {
            throw new TorrentException("TorrentManager not started");
        }
    }

    @Override
    public int getGlobalDownloadSpeed() throws TorrentException {
        return 0;
    }

    @Override
    public int getGlobalUploadSpeed() throws TorrentException {
        return 0;
    }

    @Override
    public long getTotalBytesReceived() throws TorrentException {
        verifyIsStarted();
        return 0L;
    }

    @Override
    public long getTotalBytesSent() throws TorrentException {
        verifyIsStarted();
        return 0L;
    }
}
