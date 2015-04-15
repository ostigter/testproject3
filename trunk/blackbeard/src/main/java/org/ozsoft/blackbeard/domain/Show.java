package org.ozsoft.blackbeard.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * TV show.
 * 
 * @author Oscar Stigter
 */
public class Show implements Serializable, Comparable<Show> {

    private static final long serialVersionUID = 5461098309462074246L;

    private static final Episode[] EMPTY_EPISODE_ARRAY = new Episode[0];

    /** TVRage ID. */
    private final int id;

    /** Name. */
    private final String name;

    /** TVRage link. */
    private final String link;

    /** Status. */
    private ShowStatus status;

    /** Known episodes. */
    private final Map<Integer, Episode> episodes;

    /** Timestamp of last update. */
    private transient long updateTime;

    public Show(int id, String name, String link) {
        this.id = id;
        this.name = name;
        this.link = link;

        setStatus(ShowStatus.RUNNING);

        episodes = new TreeMap<Integer, Episode>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public ShowStatus getStatus() {
        return status;
    }

    public void setStatus(ShowStatus status) {
        this.status = status;
    }

    public Episode[] getEpisodes() {
        // FIXME: Hack to sort episodes in reverse order.
        List<Episode> list = new ArrayList<Episode>(episodes.values());
        Collections.sort(list, new Comparator<Episode>() {
            @Override
            public int compare(Episode o1, Episode o2) {
                return o2.getId() - o1.getId();
            }
        });
        return list.toArray(EMPTY_EPISODE_ARRAY);
    }

    public Episode getEpisode(int id) {
        return episodes.get(id);
    }

    public void addEpisode(Episode episode) {
        episodes.put(episode.getId(), episode);
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int hashCode() {
        return id;
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
