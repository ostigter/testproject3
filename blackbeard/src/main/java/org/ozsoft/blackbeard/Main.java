package org.ozsoft.blackbeard;

import java.util.List;

import org.ozsoft.blackbeard.domain.Episode;
import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.services.ShowService;

/**
 * Test driver.
 * 
 * @author Oscar Stigter
 */
public class Main {

    public static void main(String[] args) throws Exception {
        ShowService showService = new ShowService();

        // Search all shows filtered by name.
        List<Show> shows = showService.searchShows("Arrow");
        for (Show show : shows) {
            System.out.format("%s (%d)\n", show.getName(), show.getId());
        }

        // Select best matching entry.
        Show show = shows.get(0);

        // Retrieve list of episodes.
        showService.updateEpisodes(show);
        for (Episode episode : show.getEpisodes()) {
            System.out.format("%s - %s\n", show, episode);
        }

        // Search torrents for episode.
        List<Torrent> torrents = showService.searchTorrents("arrow s03e12 720p");
        for (Torrent torrent : torrents) {
            System.out.format("'%s', %d bytes, %d seeders, %d leechers, score: %d, %s\n", torrent.title, torrent.size, torrent.seederCount,
                    torrent.leecherCount, torrent.score, torrent.magnetUri);
        }
        System.out.format("Found %d torrents.\n", torrents.size());

        if (!torrents.isEmpty()) {
            // Download torrent with highest ranking score.
            Torrent torrent = torrents.get(0);
            System.out.println("Download torrent: " + torrent);
            // showService.downloadTorrent(torrent);
        }
    }
}
