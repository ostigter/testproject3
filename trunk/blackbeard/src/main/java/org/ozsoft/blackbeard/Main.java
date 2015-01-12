package org.ozsoft.blackbeard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
import org.ozsoft.blackbeard.providers.KickAssSearchProvider;
import org.xml.sax.SAXException;

public class Main {

    private static final Pattern EPISODE_PATTERN = Pattern.compile("^.*s(\\d+)e(\\d+).*$");

    private static final Set<AbstractSearchProvider> searchProviders;

    static {
        searchProviders = new HashSet<AbstractSearchProvider>();
        searchProviders.add(new KickAssSearchProvider());
        // searchProviders.add(new BitSnoopSearchProvider());
        // searchProviders.add(new TorrentzSearchProvider());
    }

    public static void main(String[] args) throws Exception {
        // List<Show> shows = searchShows("Arrow");
        // for (Show show : shows) {
        // System.out.format("%s (%d)\n", show.name, show.id);
        // }
        // updateEpisodes(shows.get(0));

        Set<Torrent> torrents = searchTorrents("The Walking Dead s01e01 720p");
        for (Torrent torrent : torrents) {
            System.out.format("'%s', %d bytes, %d seeders, %d leechers, %s\n", torrent.title, torrent.size,
                    torrent.seederCount, torrent.leecherCount, torrent.magnetUri);
        }
        System.out.format("Found %d torrents.\n", torrents.size());
    }

    private static List<Show> searchShows(String text) {
        List<Show> shows = new ArrayList<Show>();
        String uri = "http://services.tvrage.com/feeds/search.php?show=" + text;
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

    private static void updateEpisodes(Show show) {
        String uri = "http://services.tvrage.com/feeds/episode_list.php?sid=" + show.id;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        List<Episode> episodes = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    episodes = EpisodeListParser.parse(entity.getContent());
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

        for (Episode episode : episodes) {
            System.out.format("%s - s%02de%02d\n", show.name, episode.season, episode.episode);
        }
    }

    private static Set<Torrent> searchTorrents(String text) {
        Set<Torrent> torrents = new HashSet<Torrent>();
        for (AbstractSearchProvider searchProvider : searchProviders) {
            torrents.addAll(searchProvider.search(text));
        }
        filterTorrents(torrents);
        return torrents;
    }

    private static void filterTorrents(Set<Torrent> torrents) {
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

    private static void downloadTorrent(Torrent torrent) {
        try {
            Runtime.getRuntime().exec("cmd /c start " + torrent.magnetUri);
        } catch (IOException e) {
            System.err.format("ERROR: Failed to download torrent '%s'\n", torrent.title);
        }
    }
}
