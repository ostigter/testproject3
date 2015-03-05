package org.ozsoft.blackbeard.domain;

/**
 * TV show status.
 * 
 * @author Oscar Stigter
 */
public enum ShowStatus {

    /** Running, active season (continuing). */
    RUNNING("Running"),

    /** Running, waiting for the start of the next season. */
    RETURNING("Returning"),

    /** Ended according to schedule (last episode has been aired). */
    ENDED("Ended"),

    /** Canceled, possibly ending without conclusion. */
    CANCELED("Canceled"),

    ;

    private final String name;

    ShowStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
