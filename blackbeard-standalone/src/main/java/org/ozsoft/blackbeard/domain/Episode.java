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

    /** ID = global (consequitive) episode number. */
    private final int id;

    /** Season this episode is part of. */
    private final int seasonNumber;

    /** Episode number within the season. */
    private final int episodeNumber;

    /** Title. */
    private final String title;

    /** Air date (null if not yet aired). */
    private final Date airDate;

    /** TVRage link. */
    private final String link;

    /** Status (BlackBeard). */
    private EpisodeStatus status;

    public Episode(int id, int seasonNumber, int episodeNumber, String title, Date airDate, String link) {
        this.id = id;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
        this.title = title;
        this.link = link;
        this.airDate = airDate;
        if (airDate.after(new Date())) {
            setStatus(EpisodeStatus.NOT_YET_AIRED);
        } else {
            setStatus(EpisodeStatus.NEW);
        }
    }

    public int getId() {
        return id;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getSeasonNumber() {
        return seasonNumber;
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

    public EpisodeStatus getStatus() {
        return status;
    }

    public void setStatus(EpisodeStatus status) {
        this.status = status;
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
        return String.format("s%02de%02d - %s", seasonNumber, episodeNumber, title);
    }

    @Override
    public int compareTo(Episode other) {
        // Sort episodes based on global episode number (newest first).
        // FIXME: Sort episodes newest to oldest
        return other.getId() - id;
    }
}
