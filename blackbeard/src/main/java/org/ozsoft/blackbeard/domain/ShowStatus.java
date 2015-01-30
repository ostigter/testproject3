package org.ozsoft.blackbeard.domain;

/**
 * TV show status.
 * 
 * @author Oscar Stigter
 */
public enum ShowStatus {

    /** Running, active season (continuing). */
    RUNNING,

    /** Running, waiting for the start of the next season. */
    RETURNING,

    /** Ended according to schedule (last episode has been aired). */
    ENDED,

    /** Canceled, possibly ending without conclusion. */
    CANCELED,
}
