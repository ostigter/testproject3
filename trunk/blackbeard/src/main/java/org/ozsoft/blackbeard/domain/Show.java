package org.ozsoft.blackbeard.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * TV show.
 * 
 * @author Oscar Stigter
 */
public class Show implements Serializable, Comparable<Show> {

    private static final long serialVersionUID = 5461098309462074246L;

    /** TVRage ID. */
    private final Integer id;

    /** Name. */
    private final String name;

    /** TVRage link. */
    private final String link;

    /** Known episodes. */
    private final Map<Integer, Episode> episodes;

    public Show(Integer id, String name, String link) {
        this.id = id;
        this.name = name;
        this.link = link;
        episodes = new TreeMap<Integer, Episode>();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public Episode[] getEpisodes() {
        return episodes.entrySet().toArray(new Episode[0]);
    }

    public Episode getEpisode(Long id) {
        return episodes.get(id);
    }

    public void addEpisode(Episode episode) {
        episodes.put(episode.getId(), episode);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other != null) {
            if (other instanceof Show) {
                return ((Show) other).getId() == id;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Show other) {
        return name.compareTo(other.getName());
    }
}
