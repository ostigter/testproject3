package org.ozsoft.blackbeard.domain;

/**
 * TV show episode status.
 * 
 * @author Oscar Stigter
 */
public enum EpisodeStatus {

    /** Planned, but not yet aired on TV. */
    NOT_YET_AIRED,

    /** Aired on TV, but not downloaded yet. */
    NEW,

    /** Downloaded, but not yet watched. */
    DOWNLOADED,

    /** Watched. */
    WATCHED,

}
