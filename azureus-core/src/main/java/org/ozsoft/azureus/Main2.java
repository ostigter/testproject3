package org.ozsoft.azureus;

import java.util.Locale;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.DenyAllFilter;
import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.global.GlobalManager;
import org.gudy.azureus2.core3.peer.PEPeerManagerStats;
import org.gudy.azureus2.core3.torrentdownloader.TorrentDownloader;
import org.gudy.azureus2.core3.torrentdownloader.TorrentDownloaderCallBackInterface;
import org.gudy.azureus2.core3.torrentdownloader.TorrentDownloaderFactory;
import org.gudy.azureus2.core3.torrentdownloader.impl.TorrentDownloaderManager;
import org.gudy.azureus2.ui.common.StartServer;

import com.aelitis.azureus.core.AzureusCore;
import com.aelitis.azureus.core.AzureusCoreFactory;

public class Main2 {

    private static final String TORRENT_PATH = "src/test/resources/test.torrent";
    private static final String MAGNET_URI = "magnet:?xt=urn:btih:11B23FCAD37A547F1173E12EEA6521C112C98072&dn=arrow+s03e15+hdtv+x264+lol+ettv";
    private static final String DOWNLOAD_PATH = "D:/Downloads/Torrents";
    private static final int KILOBYTE = 1024;
    private static final int MEGABYTE = 1024 * KILOBYTE;

    private static DownloadManager dm;

    public static void main(String[] args) throws Exception {
        initRootLogger();

        System.out.println("Starting...");
        StartServer start = new StartServer();
        AzureusCore core = AzureusCoreFactory.create();
        start.start();
        core.start();
        System.out.println("Started.");

        final GlobalManager gm = core.getGlobalManager();

        // Remove all torrents and associated data.
        for (DownloadManager _dm : gm.getDownloadManagers()) {
            gm.removeDownloadManager(_dm, true, true);
        }

        // Download torrent file from magnet URI.
        TorrentDownloader downloader = TorrentDownloaderFactory.create(new TorrentDownloaderCallBackInterface() {
            @Override
            public void TorrentDownloaderEvent(int state, TorrentDownloader td) {
                if (state == TorrentDownloader.STATE_FINISHED) {
                    System.out.println("Torrent file downloaded.");
                    TorrentDownloaderManager.getInstance().remove(td);
                    dm = gm.addDownloadManager(TORRENT_PATH, DOWNLOAD_PATH);
                    System.out.println("Torrent started.");

                } else {
                    if (state == TorrentDownloader.STATE_ERROR) {
                        System.err.println("ERROR: Could not download torrent file: " + td.getError());
                    }

                    TorrentDownloaderManager.getInstance().TorrentDownloaderEvent(state, td);
                }
            }
        }, MAGNET_URI, null, null, true);
        downloader.start();

        System.out.println("Downloading torrent file...");
        while (dm == null) {
            Thread.sleep(1000L);
        }

        // System.out.println("Adding torrent...");
        // DownloadManager dm = gm.addDownloadManager(TORRENT_PATH, DOWNLOAD_PATH);
        System.out.println("Torrent added.");
        System.out.println("Torrent state:        " + dm.getState());

        for (DownloadManager _dm : gm.getDownloadManagers()) {
            System.out.println("Torrent display name:   " + _dm.getDisplayName());
            System.out.println("Torrent file count:     " + _dm.getTorrent().getFileCount());
            System.out.println("Torrent size:           " + _dm.getTorrent().getSize());
            System.out.println("Torrent seed count:     " + _dm.getNbSeeds());
            System.out.println("Torrent peer count:     " + _dm.getNbPeers());
            System.out.println("Torrent state:          " + _dm.getState());
            System.out.println("Torrent isComplete:     " + _dm.isDownloadComplete(false));
        }

        Thread.sleep(3000L);

        PEPeerManagerStats pmStats = dm.getPeerManager().getStats();

        while (!dm.isDownloadComplete(false)) {
            System.out.println();
            System.out.println("Torrent state: " + dm.getState());
            System.out.println("Torrent seed count:     " + dm.getNbSeeds());
            System.out.println("Torrent peer count:     " + dm.getNbPeers());
            System.out.println("Download speed:         " + throughputToString(pmStats.getDataReceiveRate()));
            System.out.println("Upload speed:           " + throughputToString(pmStats.getDataSendRate()));
            System.out.format("Completed:              %.1f %%\n", dm.getStats().getDownloadCompleted(true) / 10.0);

            Thread.sleep(3000L);
        }

        System.out.println("Torrent finished!");
        System.out.println("Torrent state:        " + dm.getState());

        // System.out.println("Stop torrent...");
        // dm.stopIt(DownloadManager.STATE_STOPPED, false, false);
        // System.out.println("Torrent stopped.");
        // System.out.println("Torrent state:        " + dm.getState());
        //
        // System.out.println("Removing torrent (delete all)...");
        // gm.removeDownloadManager(dm, true, true);
        // System.out.println("Torrent removed.");
        // System.out.println("Torrent state:        " + dm.getState());

        System.out.println("Shutting down...");
        start.stopIt();
        core.stop();
        System.out.println("Shut down.");
    }

    private static void initRootLogger() {
        Appender app = new ConsoleAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN));
        app.setName("ConsoleAppender");
        app.addFilter(new DenyAllFilter()); // 'log off' by default
        Logger.getRootLogger().addAppender(app);
    }

    private static String throughputToString(long speed) {
        if (speed > MEGABYTE) {
            return String.format(Locale.US, "%.2f MB/s", speed / (double) MEGABYTE);
        } else {
            return String.format(Locale.US, "%d kB/s", speed / KILOBYTE);
        }
    }
}
