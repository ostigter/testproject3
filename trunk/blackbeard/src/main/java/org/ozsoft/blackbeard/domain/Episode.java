package org.ozsoft.blackbeard.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * TV show episode.
 * 
 * @author Oscar Stigter
 */
public class Episode implements Serializable, Comparable<Episode> {

    private static final long serialVersionUID = -4265981374332614409L;

    /** TVRage episode ID. */
    private final Integer id;

    /** Season number. */
    private final int season;

    /** Episode number. */
    private final int episode;

    /** Title. */
    private final String title;

    /** Air date. */
    private final Date airDate;

    /** TVRage link. */
    private final String link;

    public Episode(Integer id, int season, int episode, String title, Date airDate, String link) {
        this.id = id;
        this.season = season;
        this.episode = episode;
        this.title = title;
        this.airDate = airDate;
        this.link = link;
    }

    public Integer getId() {
        return id;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
        return episode;
    }

    public String getTitle() {
        return title;
    }

    public Date getAirDate() {
        return airDate;
    }

    public String getLink() {
        return link;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null) {
            if (other instanceof Episode) {
                return ((Episode) other).getId() == id;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return String.format("s%02de%02d - %s", season, episode, title);
    }

    @Override
    public int compareTo(Episode other) {
        return id.compareTo(other.getId());
    }
}
