package org.ozsoft.blackbeard.domain;

import java.util.List;

/**
 * TV show.
 * 
 * @author Oscar Stigter
 */
public class Show {

    /** TVRage show ID. */
    public int id;

    /** Name. */
    public String name;

    /** Known episodes. */
    public List<Episode> episodes;

    @Override
    public String toString() {
        return name;
    }
}
