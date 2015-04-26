package org.ozsoft.azureus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.global.GlobalManagerDownloadRemovalVetoException;
import org.gudy.azureus2.core3.torrent.TOTorrent;
import org.gudy.azureus2.core3.torrent.TOTorrentFile;

public class TorrentImpl implements Torrent {

    private static final Logger LOGGER = Logger.getLogger(TorrentImpl.class);

    private final String id;

    private final String name;

    private final DownloadManager dm;

    /* package */TorrentImpl(DownloadManager dm) {
        this.id = UUID.randomUUID().toString();
        this.name = dm.getDisplayName();
        this.dm = dm;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getDownloadSpeed() {
        return dm.getStats().getDataReceiveRate();
    }

    @Override
    public long getUploadSpeed() {
        return dm.getStats().getDataSendRate();
    }

    @Override
    public long getSize() {
        return dm.getSize();
    }

    @Override
    public List<File> getFiles() {
        TOTorrent torrent = dm.getTorrent();
        List<File> files = new ArrayList<File>();
        for (TOTorrentFile torrentFile : torrent.getFiles()) {
            files.add(new File(torrentFile.getRelativePath()));
        }
        return files;
    }

    @Override
    public TorrentStatus getStatus() {
        if (dm.isDownloadComplete(true)) {
            return TorrentStatus.DOWNLOADED;
        } else {
            int state = dm.getState();
            if (state == DownloadManager.STATE_STOPPED || state == DownloadManager.STATE_STOPPING) {
                return TorrentStatus.STOPPED;
            } else if (state == DownloadManager.STATE_ERROR) {
                return TorrentStatus.FAILED;
            } else {
                return TorrentStatus.DOWNLOADING;
            }
        }
    }

    @Override
    public int getPeerCount() {
        return dm.getNbPeers();
    }

    @Override
    public int getSeedCount() {
        return dm.getNbSeeds();
    }

    @Override
    public long getBytesReceived() {
        return dm.getStats().getTotalDataBytesReceived();
    }

    @Override
    public long getBytesSent() {
        return dm.getStats().getTotalDataBytesSent();
    }

    @Override
    public double getProgress() {
        return dm.getStats().getDownloadCompleted(true) / 10.0;
    }

    @Override
    public long getRemainingTime() {
        return dm.getStats().getSmoothedETA();
    }

    @Override
    public void start() throws TorrentException {
        if (dm.getState() == DownloadManager.STATE_STOPPED) {
            dm.startDownload();
            LOGGER.info(String.format("Torrent '%s' started", name));
        } else {
            throw new TorrentException("Torrent not stopped");
        }
    }

    @Override
    public void stop() throws TorrentException {
        dm.stopIt(DownloadManager.STATE_STOPPED, false, false);
        LOGGER.info(String.format("Torrent '%s' stopped", name));
    }

    @Override
    public void remove() throws TorrentException {
        try {
            dm.getGlobalManager().removeDownloadManager(dm);
        } catch (GlobalManagerDownloadRemovalVetoException e) {
            LOGGER.error(String.format("Could not remove torrent '%s'", name), e);
        }
        LOGGER.info(String.format("Torrent '%s' removed", name));
    }

    @Override
    public void delete() throws TorrentException {
        try {
            dm.stopIt(DownloadManager.STATE_STOPPED, true, true);
            dm.getGlobalManager().removeDownloadManager(dm);
            LOGGER.info(String.format("Torrent '%s' deleted", name));
        } catch (Exception e) {
            LOGGER.error(String.format("Could not delete torrent '%s'", name), e);
        }
    }

    @Override
    public String toString() {
        String downloadSpeed = TorrentUtils.throughputToString(getDownloadSpeed());
        return String.format(Locale.US, "'%s' (%s, %.1f %%, %s)", name, getStatus(), getProgress(), downloadSpeed);
    }
}
