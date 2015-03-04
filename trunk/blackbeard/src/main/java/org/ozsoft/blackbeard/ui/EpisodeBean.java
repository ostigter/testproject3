package org.ozsoft.blackbeard.ui;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ozsoft.blackbeard.domain.Episode;
import org.ozsoft.blackbeard.domain.EpisodeStatus;
import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.domain.Torrent;
import org.ozsoft.blackbeard.services.ShowService;

@Named
@SessionScoped
public class EpisodeBean implements Serializable {

    private static final long serialVersionUID = 1343652914148184382L;

    @Inject
    private ShowService showService;

    private Show show;

    private List<Torrent> torrents;

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
        showService.updateEpisodes(show);
    }

    public String deleteShow() {
        showService.deleteShow(show);
        return "listShows";
    }

    public String download(Episode episode) {
        torrents = showService.getTorrents(show, episode);
        episode.setStatus(EpisodeStatus.DOWNLOADED);
        showService.save();
        return "listTorrents";
    }

    public List<Torrent> getTorrents() {
        return torrents;
    }

    public String download(Torrent torrent) {
        System.out.println("### Download torrent: " + torrent);
        // TODO: Download episode
        return "listEpisodes";
    }

    public void watched(Episode episode) {
        episode.setStatus(EpisodeStatus.WATCHED);
        showService.save();
    }

    public void reset(Episode episode) {
        episode.setStatus(EpisodeStatus.DOWNLOADED);
        showService.save();
    }
}
