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

    /** Global (consequitive) episode number. */
    private final int episodeNumber;

    /** Episode number within the season. */
    private final int seasonNumber;

    /** Title. */
    private final String title;

    /** Air date (null if not yet aired). */
    private final Date airDate;

    /** TVRage link. */
    private final String link;

    /** Status (BlackBeard). */
    private EpisodeStatus status;

    public Episode(int episodeNumber, int seasonNumber, String title, Date airDate, String link) {
        this.episodeNumber = episodeNumber;
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.airDate = airDate;
        this.link = link;
        setStatus(EpisodeStatus.NOT_YET_AIRED);
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
        return episodeNumber;
    }

    @Override
    public boolean equals(Object other) {
        if (other != null) {
            if (other instanceof Episode) {
                return ((Episode) other).getEpisodeNumber() == episodeNumber;
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
        // Sort episodes based on global episode number (ascending).
        if (episodeNumber < other.getEpisodeNumber()) {
            return -1;
        } else if (episodeNumber > other.getEpisodeNumber()) {
            return 1;
        } else {
            return 0;
        }
    }
}
