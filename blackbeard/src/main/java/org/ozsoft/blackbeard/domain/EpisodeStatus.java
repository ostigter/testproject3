package org.ozsoft.blackbeard.domain;

/**
 * TV show episode status.
 * 
 * @author Oscar Stigter
 */
public enum EpisodeStatus {

    /** Planned, but not yet aired on TV. */
    NOT_YET_AIRED("Not aired yet"),

    /** Aired on TV, but not downloaded yet. */
    NEW("New"),

    // /** Currently being downloaded. */
    // DOWNLOADING("Downloading"),

    /** Downloaded, but not yet watched. */
    DOWNLOADED("Downloaded"),

    /** Watched. */
    WATCHED("Watched"),

    ;

    private final String name;

    EpisodeStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
