package org.ozsoft.azureus;

public class TorrentWatcher extends Thread {

    private static final long POLL_INTERVAL = 1000L;

    private final Torrent torrent;

    private final TorrentManagerImpl torrentManager;

    /* package */TorrentWatcher(Torrent torrent, TorrentManagerImpl torrentManager) {
        this.torrent = torrent;
        this.torrentManager = torrentManager;

        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            if (torrent.getStatus() == TorrentStatus.DOWNLOADED) {
                torrentManager.fireDownloadCompleted(torrent);
                break;
            } else if (torrent.getStatus() == TorrentStatus.FAILED) {
                torrentManager.fireDownloadFailed(torrent);
                break;
            } else {
                try {
                    Thread.sleep(POLL_INTERVAL);
                } catch (InterruptedException e) {
                    // Safe to ignore.
                }
            }
        }
    }
}
