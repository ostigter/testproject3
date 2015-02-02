package org.ozsoft.blackbeard.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.domain.ShowStatus;

public class Configuration implements Serializable {

    private static final long serialVersionUID = 6200553605465987303L;

    private final Map<Integer, Show> shows;

    public Configuration() {
        shows = new HashMap<Integer, Show>();
    }

    public void load() {
        // FIXME: Load config from database
        shows.clear();

        Show show = new Show(30715, "Arrow", "http://www.tvrage.com/Arrow");
        show.setStatus(ShowStatus.RUNNING);
        shows.put(show.getId(), show);

        show = new Show(3332, "Doctor Who", "http://www.tvrage.com/DoctorWho_2005");
        show.setStatus(ShowStatus.RETURNING);
        shows.put(show.getId(), show);
    }

    public void save() {
        // TODO
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
}
