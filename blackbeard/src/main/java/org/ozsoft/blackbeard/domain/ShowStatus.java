package org.ozsoft.blackbeard.domain;

/**
 * TV show status (TVMaze).
 * 
 * @author Oscar Stigter
 */
public enum ShowStatus {

    /** Running, active season (continuing). */
    RUNNING("Running"),

    /** Ended or canceled. */
    ENDED("Ended"),

    /** Unknown (to be determined). */
    UNKNOWN("Unknown"),

    ;

    private final String name;

    ShowStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ShowStatus parse(String status) {
        if (status != null) {
            status = status.toUpperCase();
            if (status.equals("RUNNING")) {
                return ShowStatus.RUNNING;
            } else if (status.equals("ENDED")) {
                return ShowStatus.ENDED;
            } else {
                return ShowStatus.UNKNOWN;
            }
        } else {
            return ShowStatus.UNKNOWN;
        }
    }
}
