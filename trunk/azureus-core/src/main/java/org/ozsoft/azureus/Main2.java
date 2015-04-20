package org.ozsoft.azureus;

import java.io.File;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.varia.DenyAllFilter;
import org.gudy.azureus2.core3.torrentdownloader.TorrentDownloader;
import org.gudy.azureus2.core3.torrentdownloader.TorrentDownloaderCallBackInterface;
import org.gudy.azureus2.core3.torrentdownloader.TorrentDownloaderFactory;
import org.gudy.azureus2.core3.torrentdownloader.impl.TorrentDownloaderManager;
import org.gudy.azureus2.ui.common.StartServer;

import com.aelitis.azureus.core.AzureusCore;
import com.aelitis.azureus.core.AzureusCoreFactory;

public class Main2 {

    private static final String MAGNET_URI = "magnet:?xt=urn:btih:53896C2A6391A69A672041139E023C018C0F4AFF&dn=arrow+s03e19+hdtv+x264+lol+ettv&tr=udp%3A%2F%2F9.rarbg.to%3A2710%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337";

    private static File torrentFile = null;

    public static void main(String[] args) throws Exception {
        initRootLogger();

        System.out.println("Starting...");
        StartServer start = new StartServer();
        AzureusCore core = AzureusCoreFactory.create();
        start.start();
        core.start();
        System.out.println("Started.");

        // Download torrent file from magnet URI.
        TorrentDownloader downloader = TorrentDownloaderFactory.create(new TorrentDownloaderCallBackInterface() {
            @Override
            public void TorrentDownloaderEvent(int state, TorrentDownloader td) {
                if (state == TorrentDownloader.STATE_FINISHED) {
                    torrentFile = td.getFile();
                    TorrentDownloaderManager.getInstance().remove(td);

                } else if (state == TorrentDownloader.STATE_ERROR) {
                    System.err.println("ERROR: Could not download torrent file: " + td.getError());
                }

                TorrentDownloaderManager.getInstance().TorrentDownloaderEvent(state, td);
            }
        }, MAGNET_URI, null, null, false);
        downloader.start();

        System.out.println("Downloading torrent file...");
        while (torrentFile == null) {
            Thread.sleep(1000L);
        }
        System.out.println("Torrent file downloaded: " + torrentFile.getAbsolutePath());

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
}
