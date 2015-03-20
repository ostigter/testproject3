package org.ozsoft.azureus;

public class Main {

    public static void main(String[] args) {
        TorrentManager torrentManager = new DefaultTorrentManager();

        try {
            torrentManager.start();

            torrentManager.stop();

        } catch (TorrentException e) {
            System.err.println("ERROR: " + e);
            e.printStackTrace(System.err);
        }
    }
}
