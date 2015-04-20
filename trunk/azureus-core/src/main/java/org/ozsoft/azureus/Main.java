package org.ozsoft.azureus;

import java.io.File;

public class Main implements TorrentListener {

    private static final String TORRENT_PATH = "test.torrent";

    private static final String MAGNET_URI = "magnet:?xt=urn:btih:53896C2A6391A69A672041139E023C018C0F4AFF&dn=arrow+s03e19+hdtv+x264+lol+ettv&tr=udp%3A%2F%2F9.rarbg.to%3A2710%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337";

    private static final String SAVE_PATH = "D:/Downloads/Torrents";

    public static void main(String[] args) {
        new Main().run(args);
    }

    private void run(String[] args) {
        TorrentManager tm = TorrentManagerFactory.createTorrentManager();

        tm.addTorrentListener(this);

        try {
            tm.start();

            for (Torrent torrent : tm.getTorrents()) {
                torrent.delete();
            }

            tm.downloadTorrent(MAGNET_URI, SAVE_PATH);
            sleep(45000L);

            while (tm.hasActiveDownloads()) {
                for (Torrent torrent : tm.getTorrents()) {
                    System.out.println("Torrent: " + torrent);
                }
                sleep(3000L);
            }

            System.out.println("No more active downloads.");

            tm.stop();

        } catch (TorrentException e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.err);
        }
    }

    @Override
    public void downloadCompleted(Torrent torrent) {
        System.out.println("Download completed: " + torrent);

        for (File file : torrent.getFiles()) {
            System.out.println(file);
        }

        try {
            torrent.remove();
        } catch (TorrentException e) {
            e.printStackTrace(System.err);
        }
    }

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
