package org.ozsoft.azureus;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.global.GlobalManager;
import org.gudy.azureus2.ui.common.StartServer;

import com.aelitis.azureus.core.AzureusCore;
import com.aelitis.azureus.core.AzureusCoreFactory;

public class TorrentManagerImpl implements TorrentManager {

    private static final long TORRENT_TIMEOUT = 10000L;

    private static final Logger LOGGER = Logger.getLogger(TorrentManagerImpl.class);

    private static final String DOWNLOAD_PATH = "D:/Downloads/torrents";

    private final Map<String, Torrent> torrents;

    private StartServer azureusServer;

    private AzureusCore azureusCore;

    private GlobalManager globalManager;

    private boolean isStarted;

    /* package */TorrentManagerImpl() {
        torrents = new HashMap<String, Torrent>();
        isStarted = false;
    }

    @Override
    public void start() throws TorrentException {
        if (!isStarted) {
            LOGGER.debug("Starting");
            try {
                azureusServer = new StartServer();
                azureusServer.start();

                azureusCore = AzureusCoreFactory.create();
                azureusCore.start();

                globalManager = azureusCore.getGlobalManager();

                isStarted = true;
                LOGGER.info("Started");

            } catch (Exception e) {
                throw new TorrentException("Could not start Azureus", e);
            }
        }
    }

    @Override
    public void stop() throws TorrentException {
        if (isStarted) {
            LOGGER.debug("Stopping");
            try {
                azureusCore.stop();
                azureusServer.stopIt();
                isStarted = false;
                LOGGER.info("Stopped");
            } catch (Exception e) {
                throw new TorrentException("Could not stop Azureus", e);
            }
        }
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public Torrent downloadTorrent(String location) throws TorrentException {
        verifyIsStarted();
        if (location.startsWith("magnet:") || location.startsWith("http://")) {
            return downloadFromRemoteTorrent(location);
        } else {
            return downloadFromLocalTorrent(location);
        }
    }

    private Torrent downloadFromRemoteTorrent(String uri) throws TorrentException {
        // TODO: Download remote torrent (magnet URI or HTTP)
        return null;
    }

    private Torrent downloadFromLocalTorrent(String path) throws TorrentException {
        File torrentFile = new File(path);
        if (!torrentFile.isFile()) {
            throw new TorrentException("Torrent file not found: " + path);
        }

        DownloadManager dm = null;
        try {
            dm = globalManager.addDownloadManager(torrentFile.getAbsolutePath(), DOWNLOAD_PATH);
            long startTime = System.currentTimeMillis();
            while (dm.getState() != DownloadManager.STATE_DOWNLOADING) {
                long duration = System.currentTimeMillis() - startTime;
                if (duration > TORRENT_TIMEOUT) {
                    // Download takes too long to start; abort and delete.
                    dm.stopIt(DownloadManager.STATE_STOPPED, true, true);
                    throw new TorrentException("Download failed to start time-out)");
                }
            }
        } catch (Exception e) {
            throw new TorrentException(String.format("Could not start torrent from file '%s'", path), e);
        }

        Torrent torrent = new TorrentImpl(dm);
        torrents.put(torrent.getId(), torrent);

        return torrent;
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
    public int getGlobalDownloadSpeed() throws TorrentException {
        verifyIsStarted();
        return globalManager.getStats().getDataReceiveRate();
    }

    @Override
    public int getGlobalUploadSpeed() throws TorrentException {
        verifyIsStarted();
        return globalManager.getStats().getDataSendRate();
    }

    @Override
    public long getTotalBytesReceived() throws TorrentException {
        verifyIsStarted();
        return globalManager.getStats().getTotalDataBytesReceived();
    }

    @Override
    public long getTotalBytesSent() throws TorrentException {
        verifyIsStarted();
        return globalManager.getStats().getTotalDataBytesSent();
    }

    private void verifyIsStarted() throws TorrentException {
        if (!isStarted) {
            throw new TorrentException("TorrentManager not started");
        }
    }
}
