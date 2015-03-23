package org.ozsoft.azureus;

public class Main {

    private static final String TORRENT_PATH = "src/test/resources/test.torrent";

    // private static final String MAGNET_URI =
    // "magnet:?xt=urn:btih:11B23FCAD37A547F1173E12EEA6521C112C98072&dn=arrow+s03e15+hdtv+x264+lol+ettv";
    // private static final String DOWNLOAD_PATH = "D:/Downloads/Torrents";

    public static void main(String[] args) {
        TorrentManager tm = TorrentManagerFactory.createTorrentManager();

        tm.addTorrentListener(new TorrentListener() {
            @Override
            public void downloadCompleted(Torrent torrent) {
                System.out.println("Download completed: " + torrent);
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
        });

        try {
            tm.start();

            tm.downloadTorrent(TORRENT_PATH);

            while (tm.hasActiveDownloads()) {
                for (Torrent torrent : tm.getTorrents()) {
                    System.out.println("Torrent: " + torrent);
                }

                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    // Safe to ignore.
                }
            }

            System.out.println("No more active downloads.");

            tm.stop();

        } catch (TorrentException e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.err);
        }
    }
}
