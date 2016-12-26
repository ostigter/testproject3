package org.ozsoft.azureus;

import java.io.File;

/**
 * Manual test driver.
 * 
 * @author Oscar Stigter
 */
public class Main implements TorrentListener {

    // private static final String TORRENT_PATH = "src/test/resources/torrent/test.torrent";

    private static final String MAGNET_URI = "magnet:?xt=urn:btih:b00f7b95cf527d30b36b3fa3946817d2a9b958d7";

    private static final String SAVE_PATH = "D:/Downloads/Torrents";

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        TorrentManager tm = TorrentManagerFactory.createTorrentManager();

        tm.addTorrentListener(this);

        try {
            // Start Azureus.
            tm.start();

            // Stop and delete any previous downloads.
            for (Torrent torrent : tm.getTorrents()) {
                torrent.delete();
            }

            // Start download from local torrent file.
            // tm.downloadTorrent(TORRENT_PATH, SAVE_PATH);

            // Start download from magnet link.
            tm.downloadTorrent(MAGNET_URI, SAVE_PATH);
            // Wait for remote torrent file to be downloaded.
            sleep(40000L);

            // Show download status until completed.
            while (tm.hasActiveDownloads()) {
                for (Torrent torrent : tm.getTorrents()) {
                    System.out.println("Torrent: " + torrent);
                }
                sleep(3000L);
            }

            System.out.println("No more active downloads.");

            // Shutdown Azureus.
            tm.stop();

        } catch (TorrentException e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.err);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ozsoft.azureus.TorrentListener#downloadCompleted(org.ozsoft.azureus.Torrent)
     */
    @Override
    public void downloadCompleted(Torrent torrent) {
        System.out.println("Download completed: " + torrent);

        // List downloaded files.
        for (File file : torrent.getFiles()) {
            System.out.println(file);
        }

        // Remove download, but keep downloaded files.
        try {
            torrent.remove();
        } catch (TorrentException e) {
            e.printStackTrace(System.err);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.ozsoft.azureus.TorrentListener#downloadFailed(org.ozsoft.azureus.Torrent)
     */
    @Override
    public void downloadFailed(Torrent torrent) {
        System.err.println("Download failed: " + torrent);
        try {
            torrent.remove();
        } catch (TorrentException e) {
            e.printStackTrace(System.err);
        }
    }

    private static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            // Safe to ignore.
        }
    }
}
