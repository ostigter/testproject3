package org.ozsoft.blackbeard.services;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.http.HttpStatus;
import org.ozsoft.blackbeard.data.Configuration;
import org.ozsoft.blackbeard.domain.Episode;
import org.ozsoft.blackbeard.domain.EpisodeStatus;
import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.parsers.EpisodeListParser;
import org.ozsoft.blackbeard.parsers.ParseException;
import org.ozsoft.blackbeard.parsers.ShowListParser;
import org.ozsoft.blackbeard.providers.ExtraTorrentSearchProvider;
import org.ozsoft.blackbeard.providers.SearchProvider;
import org.ozsoft.blackbeard.util.http.HttpClient;
import org.ozsoft.blackbeard.util.http.HttpResponse;

/**
 * TV show service.
 * 
 * @author Oscar Stigter
 */
@Named
@ApplicationScoped
public class ShowService implements Serializable {

    private static final long serialVersionUID = 9026865382985469495L;

    private static final String TVMAZE_SHOW_SEARCH_URL = "http://api.tvmaze.com/search/shows?q=%s";

    private static final String TVMAZE_EPISODE_LIST_URL = "http://api.tvmaze.com/shows/%d/episodes?specials=1";

    private static final Pattern EPISODE_PATTERN = Pattern.compile("^.*[sS](\\d+)[eE](\\d+).*$");

    private static final String UTF8 = "UTF-8";

    private static final long MIN_UPDATE_INTERVAL = 60 * 60 * 1000L; // 1 hour

    // private static final String PROXY_HOST = "146.106.91.10";
    // private static final int PROXY_PORT = 8080;
    // private static final String PROXY_USERNAME = "";
    // private static final String PROXY_PASSWORD = "";

    private static final Set<SearchProvider> searchProviders;

    private final HttpClient httpClient;

    private final Configuration config;

    /**
     * Static initializer.
     */
    static {
        // Set torrent search providers.
        searchProviders = new HashSet<SearchProvider>();
        searchProviders.add(new ExtraTorrentSearchProvider());
        // searchProviders.add(new BitSnoopSearchProvider());
    }

    public ShowService() {
        config = new Configuration();
        config.load();

        httpClient = new HttpClient();
        // httpClient = new HttpClient(PROXY_HOST, PROXY_PORT, PROXY_USERNAME, PROXY_PASSWORD);
    }

    public Show[] getShows() {
        return config.getShows();
    }

    public Show getShow(int id) {
        return config.getShow(id);
    }

    public void addShow(Show show) {
        config.addShow(show);
        save();
    }

    public void deleteShow(Show show) {
        config.deleteShow(show);
        save();
    }

    public void save() {
        config.save();
    }

    /**
     * Searches for shows by name.
     * 
     * @param text
     *            Part of the show's name.
     * @return
     */
    public List<Show> searchShows(String text) {
        List<Show> shows = new ArrayList<Show>();
        String uri = String.format(TVMAZE_SHOW_SEARCH_URL, encodeUrl(text));
        try {
            HttpResponse httpResponse = httpClient.executeGet(uri);
            int statusCode = httpResponse.getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                shows = ShowListParser.parse(httpResponse.getBody());
            } else {
                System.err.format("ERROR: Could not retrieve list of shows from URI '%s' (HTTP status: %d)\n", uri, statusCode);
            }
        } catch (IOException | ParseException e) {
            System.err.format("ERROR: Could not retrieve or parse list of shows from URI '%s'\n", uri);
            e.printStackTrace();
        }

        return shows;
    }

    public void updateEpisodes(Show show) {
        if ((System.currentTimeMillis() - show.getUpdateTime()) > MIN_UPDATE_INTERVAL) {
            String uri = String.format(TVMAZE_EPISODE_LIST_URL, show.getId());
            try {
                HttpResponse httpResponse = httpClient.executeGet(uri);
                int statusCode = httpResponse.getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    List<Episode> episodes = EpisodeListParser.parse(httpResponse.getBody());
                    for (Episode episode : episodes) {
                        Episode existingEpisode = show.getEpisode(episode.getId());
                        if (existingEpisode != null) {
                            EpisodeStatus currentStatus = existingEpisode.getStatus();
                            if (currentStatus == EpisodeStatus.DOWNLOADED || currentStatus == EpisodeStatus.WATCHED) {
                                episode.setStatus(currentStatus);
                            }
                        }
                        show.addEpisode(episode);
                    }
                } else {
                    System.err.format("ERROR: Could not retrieve or parse episode list from URI '%s' (HTTP status: %d)\n", uri, statusCode);
                }
            } catch (IOException | ParseException e) {
                System.err.format("ERROR: Could not parse episode list from URI '%s'\n", uri);
                e.printStackTrace();
            }

            show.setUpdateTime(System.currentTimeMillis());
            save();
        }
    }

    public List<Torrent> getTorrents(Show show, Episode episode) {
        String searchText = String.format("%s s%02de%02d", show.getName(), episode.getSeasonNumber(), episode.getEpisodeNumber());
        Set<Torrent> torrents = new TreeSet<Torrent>();
        for (SearchProvider searchProvider : searchProviders) {
            torrents.addAll(searchProvider.search(searchText, httpClient));
        }

        filterTorrents(torrents);

        return new ArrayList<Torrent>(torrents);
    }

    private void filterTorrents(Set<Torrent> torrents) {
        Iterator<Torrent> it = torrents.iterator();
        while (it.hasNext()) {
            Torrent torrent = it.next();

            // Clean up the title (e.g. replacing all dots with spaces).
            String title = torrent.getTitle().replaceAll("\\.", " ").toLowerCase();

            // Filter out torrents that are not TV show episodes.
            Matcher m = EPISODE_PATTERN.matcher(title);
            if (!m.matches()) {
                it.remove();
            }
        }
    }

    public boolean downloadTorrent(Torrent torrent) {
        boolean isDownloaded = false;
        try {
            Runtime.getRuntime().exec("cmd /c start " + torrent.getMagnetUri());
            isDownloaded = true;
        } catch (IOException e) {
            System.err.format("ERROR: Failed to download torrent '%s'\n", torrent);
        }
        return isDownloaded;
    }

    private static String encodeUrl(String uri) {
        try {
            uri = URLEncoder.encode(uri, UTF8);
        } catch (UnsupportedEncodingException e) {
            // Can never happen, because UTF-8 is supported by default.
        }
        return uri;
    }
}
