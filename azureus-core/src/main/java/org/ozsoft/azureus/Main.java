package org.ozsoft.azureus;

import java.io.File;

/**
 * Manual test driver.
 * 
 * @author Oscar Stigter
 */
public class Main implements TorrentListener {

    // private static final String TORRENT_PATH = "src/test/resources/torrent/test.torrent";

    private static final String MAGNET_URI = "magnet:?xt=urn:btih:53896C2A6391A69A672041139E023C018C0F4AFF&dn=arrow+s03e19+hdtv+x264+lol+ettv&tr=udp%3A%2F%2F9.rarbg.to%3A2710%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337";

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
