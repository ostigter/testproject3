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
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpStatus;
import org.ozsoft.blackbeard.data.Configuration;
import org.ozsoft.blackbeard.domain.Episode;
import org.ozsoft.blackbeard.domain.EpisodeStatus;
import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.parsers.EpisodeListParser;
import org.ozsoft.blackbeard.parsers.ShowListParser;
import org.ozsoft.blackbeard.providers.AbstractSearchProvider;
import org.ozsoft.blackbeard.providers.BitSnoopSearchProvider;
import org.ozsoft.blackbeard.providers.KickAssSearchProvider;
import org.ozsoft.blackbeard.util.http.HttpClient;
import org.ozsoft.blackbeard.util.http.HttpResponse;
import org.xml.sax.SAXException;

/**
 * TV show service.
 * 
 * @author Oscar Stigter
 */
@Named
@ApplicationScoped
public class ShowService implements Serializable {

    private static final long serialVersionUID = 9026865382985469495L;

    private static final String TVRAGE_SHOW_SEARCH_URL = "http://services.tvrage.com/feeds/search.php?show=%s";

    private static final String TVRAGE_EPISODE_LIST_URL = "http://services.tvrage.com/feeds/episode_list.php?sid=%d";

    private static final Pattern EPISODE_PATTERN = Pattern.compile("^.*s(\\d+)e(\\d+).*$");

    private static final String UTF8 = "UTF-8";

    private static final long MIN_UPDATE_INTERVAL = 60 * 60 * 1000L; // 1 hour

    // private static final String PROXY_HOST = "146.106.91.10";
    // private static final int PROXY_PORT = 8080;
    // private static final String PROXY_USERNAME = "";
    // private static final String PROXY_PASSWORD = "";

    private static final Set<AbstractSearchProvider> searchProviders;

    private final HttpClient httpClient;

    private final Configuration config;

    /**
     * Static initializer.
     */
    static {
        // Set torrent search providers.
        searchProviders = new HashSet<AbstractSearchProvider>();
        searchProviders.add(new KickAssSearchProvider());
        searchProviders.add(new BitSnoopSearchProvider());
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
        String uri = String.format(TVRAGE_SHOW_SEARCH_URL, encodeUrl(text));
        try {
            HttpResponse httpResponse = httpClient.executeGet(uri);
            int statusCode = httpResponse.getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                shows = ShowListParser.parse(httpResponse.getBody());
            } else {
                System.err.format("ERROR: Could not retrieve list of shows from URI '%s' (HTTP status: %d)\n", uri, statusCode);
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            System.err.format("ERROR: Could not retrieve or parse list of shows from URI '%s'\n", uri);
            e.printStackTrace();
        }

        return shows;
    }

    public void updateEpisodes(Show show) {
        if ((System.currentTimeMillis() - show.getUpdateTime()) > MIN_UPDATE_INTERVAL) {
            String uri = String.format(TVRAGE_EPISODE_LIST_URL, show.getId());
            try {
                HttpResponse httpResponse = httpClient.executeGet(uri);
                int statusCode = httpResponse.getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    List<Episode> episodes = EpisodeListParser.parse(httpResponse.getBody());
                    for (Episode episode : episodes) {
                        Episode existingEpisode = show.getEpisode(episode.getEpisodeNumber());
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
            } catch (IOException | SAXException | ParserConfigurationException e) {
                System.err.format("ERROR: Could not parse episode list from URI '%s'\n", uri);
                e.printStackTrace();
            }

            show.setUpdateTime(System.currentTimeMillis());
            save();
        }
    }

    public List<Torrent> searchTorrents(String text) {
        Set<Torrent> torrents = new TreeSet<Torrent>();
        for (AbstractSearchProvider searchProvider : searchProviders) {
            torrents.addAll(searchProvider.search(text, httpClient));
        }

        filterTorrents(torrents);

        return new ArrayList<Torrent>(torrents);
    }

    public void filterTorrents(Set<Torrent> torrents) {
        Iterator<Torrent> it = torrents.iterator();
        while (it.hasNext()) {
            Torrent torrent = it.next();
            String name = torrent.title.replaceAll("\\.", " ").toLowerCase();
            Matcher m = EPISODE_PATTERN.matcher(name);
            if (m.matches()) {
                // int season = Integer.parseInt(m.group(1));
                // int episode = Integer.parseInt(m.group(2));
                // System.out.format("%s (season: %d, episode: %d)\n", name, season, episode);
            } else {
                it.remove();
            }
        }
    }

    public void downloadTorrent(Torrent torrent) {
        try {
            Runtime.getRuntime().exec("cmd /c start " + torrent.magnetUri);
        } catch (IOException e) {
            System.err.format("ERROR: Failed to download torrent '%s'\n", torrent.title);
        }
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
