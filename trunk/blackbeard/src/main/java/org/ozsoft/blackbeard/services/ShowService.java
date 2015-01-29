package org.ozsoft.blackbeard.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.ozsoft.blackbeard.domain.Episode;
import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.parsers.EpisodeListParser;
import org.ozsoft.blackbeard.parsers.ShowListParser;
import org.ozsoft.blackbeard.providers.AbstractSearchProvider;
import org.ozsoft.blackbeard.providers.BitSnoopSearchProvider;
import org.ozsoft.blackbeard.providers.KickAssSearchProvider;
import org.xml.sax.SAXException;

/**
 * TV show service.
 * 
 * @author Oscar Stigter
 */
public class ShowService {

    private static final String TVRAGE_SHOW_SEARCH_URL = "http://services.tvrage.com/feeds/search.php?show=%s";

    private static final String TVRAGE_EPISODE_LIST_URL = "http://services.tvrage.com/feeds/episode_list.php?sid=%d";

    private static final Pattern EPISODE_PATTERN = Pattern.compile("^.*s(\\d+)e(\\d+).*$");

    private static final Set<AbstractSearchProvider> searchProviders;

    /**
     * Static initializer.
     */
    static {
        // Set torrent search providers.
        searchProviders = new HashSet<AbstractSearchProvider>();
        searchProviders.add(new KickAssSearchProvider());
        searchProviders.add(new BitSnoopSearchProvider());
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
        String uri = String.format(TVRAGE_SHOW_SEARCH_URL, text);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    shows = ShowListParser.parse(entity.getContent());
                }
            }
        } catch (IOException e) {
            System.err.format("ERROR: Could not retrieve list of shows from URI '%s'\n", uri);
            e.printStackTrace();
        } catch (SAXException | ParserConfigurationException e) {
            System.err.format("ERROR: Could not parse list of shows from URI '%s'\n", uri);
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(response);
        }

        return shows;
    }

    public void updateEpisodes(Show show) {
        String uri = String.format(TVRAGE_EPISODE_LIST_URL, show.getId());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                List<Episode> episodes = EpisodeListParser.parse(response.getEntity().getContent());
                for (Episode episode : episodes) {
                    Episode existingEpisode = show.getEpisode(episode.getEpisodeNumber());
                    if (existingEpisode != null) {
                        episode.setStatus(existingEpisode.getStatus());
                    }
                    show.addEpisode(episode);
                }
            } else {
                System.err.format("ERROR: Could not retrieve episode list from URI '%s' (HTTP status: %d)\n", uri,
                        statusCode);
            }
        } catch (IOException e) {
            System.err.format("ERROR: Could not retrieve episode list from URI '%s'\n", uri);
            e.printStackTrace();
        } catch (SAXException | ParserConfigurationException e) {
            System.err.format("ERROR: Could not parse episode list from URI '%s'\n", uri);
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(response);
        }
    }

    public List<Torrent> searchTorrents(String text) {
        Set<Torrent> torrents = new TreeSet<Torrent>();
        for (AbstractSearchProvider searchProvider : searchProviders) {
            torrents.addAll(searchProvider.search(text));
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
}
