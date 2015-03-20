package org.ozsoft.azureus;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultTorrentManager implements TorrentManager {

    private final Map<String, Torrent> torrents;

    private boolean isStarted;

    public DefaultTorrentManager() {
        torrents = new HashMap<String, Torrent>();
        isStarted = false;
    }

    @Override
    public void start() throws TorrentException {
        if (!isStarted) {
            isStarted = true;
        }
    }

    @Override
    public void stop() throws TorrentException {
        if (isStarted) {
            isStarted = false;
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

    @Override
    public void startTorrent(Torrent torrent) throws TorrentException {
        verifyIsStarted();
    }

    @Override
    public void stopTorrent(Torrent torrent) throws TorrentException {
        verifyIsStarted();
    }

    @Override
    public void removeTorrent(Torrent torrent) throws TorrentException {
        verifyIsStarted();
    }

    @Override
    public void deleteTorrent(Torrent torrent) throws TorrentException {
        verifyIsStarted();
    }

    @Override
    public int getDownloadSpeed() throws TorrentException {
        verifyIsStarted();
        return 0;
    }

    @Override
    public int getUploadSpeed() throws TorrentException {
        verifyIsStarted();
        return 0;
    }

    @Override
    public long getTotalBytesReceived() throws TorrentException {
        verifyIsStarted();
        return 0;
    }

    @Override
    public long getTotalBytesSent() throws TorrentException {
        verifyIsStarted();
        return 0;
    }

    private void verifyIsStarted() throws TorrentException {
        if (!isStarted) {
            throw new TorrentException("TorrentManager not started");
        }
    }
}
