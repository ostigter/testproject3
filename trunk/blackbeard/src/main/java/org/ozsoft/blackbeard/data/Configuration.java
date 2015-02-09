package org.ozsoft.blackbeard.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.ozsoft.blackbeard.domain.Show;

public class Configuration implements Serializable {

    private static final long serialVersionUID = 6200553605465987303L;

    private final Map<Integer, Show> shows;

    public Configuration() {
        shows = new HashMap<Integer, Show>();
    }

    public void load() {
        shows.clear();
        // TODO: Load config
    }

    public void save() {
        // TODO: Save config
    }

    public Show[] getShows() {
        return new TreeSet<Show>(shows.values()).toArray(new Show[0]);
    }

    public Show getShow(int id) {
        return shows.get(id);
    }

    public void addShow(Show show) {
        shows.put(show.getId(), show);
    }

    public void deleteShow(int id) {
        shows.remove(id);
    }
}
