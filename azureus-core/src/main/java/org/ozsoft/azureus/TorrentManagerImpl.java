package org.ozsoft.azureus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.global.GlobalManager;
import org.gudy.azureus2.core3.global.GlobalManagerDownloadRemovalVetoException;
import org.gudy.azureus2.core3.torrentdownloader.TorrentDownloader;
import org.gudy.azureus2.core3.torrentdownloader.TorrentDownloaderCallBackInterface;
import org.gudy.azureus2.core3.torrentdownloader.TorrentDownloaderFactory;
import org.gudy.azureus2.core3.torrentdownloader.impl.TorrentDownloaderManager;
import org.gudy.azureus2.ui.common.StartServer;

import com.aelitis.azureus.core.AzureusCore;
import com.aelitis.azureus.core.AzureusCoreFactory;

public class TorrentManagerImpl implements TorrentManager {

    private static final long TORRENT_TIMEOUT = 10000L;

    private static final Logger LOGGER = Logger.getLogger(TorrentManagerImpl.class);

    private StartServer azureusServer;

    private AzureusCore azureusCore;

    private GlobalManager globalManager;

    private final Map<Torrent, TorrentWatcher> watchers;

    private final Set<TorrentListener> listeners;

    private boolean isStarted;

    /* package */TorrentManagerImpl() {
        watchers = new HashMap<Torrent, TorrentWatcher>();
        listeners = new HashSet<TorrentListener>();
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

                initWatchers();

                isStarted = true;
                LOGGER.info("Started");

            } catch (Exception e) {
                String msg = "Could not start Azureus";
                LOGGER.error(msg, e);
                throw new TorrentException(msg, e);
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
                String msg = "Could not gracefully stop Azureus";
                LOGGER.error(msg, e);
                throw new TorrentException(msg, e);
            }
        }
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public void addTorrentListener(TorrentListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeTorrentListener(TorrentListener listener) {
        listeners.remove(listener);
    }

    @Override
    public Torrent downloadTorrent(String location, String savePath) throws TorrentException {
        verifyIsStarted();
        if (location.startsWith("magnet:") || location.startsWith("http://")) {
            return downloadFromRemoteTorrent(location, savePath);
        } else {
            return downloadFromLocalTorrent(location, savePath);
        }
    }

    private Torrent downloadFromLocalTorrent(String path, String savePath) throws TorrentException {
        File torrentFile = new File(path);
        if (!torrentFile.isFile()) {
            throw new TorrentException("Torrent file not found: " + path);
        }

        DownloadManager dm = null;
        try {
            dm = globalManager.addDownloadManager(torrentFile.getAbsolutePath(), savePath);
            long startTime = System.currentTimeMillis();
            while (dm.getState() != DownloadManager.STATE_DOWNLOADING) {
                long duration = System.currentTimeMillis() - startTime;
                if (duration > TORRENT_TIMEOUT) {
                    // Download takes too long to start; abort and delete.
                    try {
                        dm.getGlobalManager().removeDownloadManager(dm);
                    } catch (GlobalManagerDownloadRemovalVetoException e) {
                        // Nothing we can do here.
                    }
                    throw new TorrentException("Download failed to start time-out)");
                }
            }

            Torrent torrent = new TorrentImpl(dm);

            TorrentWatcher watcher = new TorrentWatcher(torrent, this);
            watchers.put(torrent, watcher);
            watcher.start();

            LOGGER.info(String.format("Download started from torrent file '%s'", torrentFile));

            return torrent;

        } catch (Exception e) {
            String msg = String.format("Could not start torrent from file '%s'", path);
            throw new TorrentException(msg, e);
        }
    }

    private Torrent downloadFromRemoteTorrent(final String uri, final String savePath) throws TorrentException {
        TorrentDownloader downloader = TorrentDownloaderFactory.create(new TorrentDownloaderCallBackInterface() {
            @Override
            public void TorrentDownloaderEvent(int state, TorrentDownloader td) {
                if (state == TorrentDownloader.STATE_FINISHED) {
                    File torrentFile = td.getFile();
                    LOGGER.info("Torrent file downloaded: " + torrentFile);
                    TorrentDownloaderManager.getInstance().remove(td);
                    try {
                        downloadFromLocalTorrent(torrentFile.getAbsolutePath(), savePath);
                    } catch (TorrentException e) {
                        LOGGER.error("Could not start download", e);
                    }
                } else if (state == TorrentDownloader.STATE_ERROR) {
                    LOGGER.error("Could not download torrent file: " + td.getError());
                }
                TorrentDownloaderManager.getInstance().TorrentDownloaderEvent(state, td);
            }
        }, uri, null, null, false);
        downloader.start();

        LOGGER.info(String.format("Download of torrent from '%s' started", uri));

        return null;
    }

    @Override
    public List<Torrent> getTorrents() throws TorrentException {
        verifyIsStarted();
        List<Torrent> torrents = new ArrayList<Torrent>();
        for (DownloadManager dm : globalManager.getDownloadManagers()) {
            torrents.add(new TorrentImpl(dm));
        }
        return torrents;
    }

    @Override
    public boolean hasActiveDownloads() {
        for (DownloadManager dm : globalManager.getDownloadManagers()) {
            if (!dm.isDownloadComplete(true)) {
                return true;
            }
        }
        return false;
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

    /* package */void fireDownloadCompleted(Torrent torrent) {
        for (TorrentListener listener : listeners) {
            listener.downloadCompleted(torrent);
        }
    }

    /* package */void fireDownloadFailed(Torrent torrent) {
        for (TorrentListener listener : listeners) {
            listener.downloadFailed(torrent);
        }
    }

    private void initWatchers() {
        for (DownloadManager dm : globalManager.getDownloadManagers()) {
            Torrent torrent = new TorrentImpl(dm);
            TorrentWatcher watcher = new TorrentWatcher(torrent, this);
            watchers.put(torrent, watcher);
            watcher.start();
        }
    }

    private void verifyIsStarted() throws TorrentException {
        if (!isStarted) {
            throw new TorrentException("TorrentManager not started");
        }
    }
}
