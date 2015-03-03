package org.ozsoft.blackbeard.ui;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ozsoft.blackbeard.domain.Episode;
import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.services.ShowService;

@Named
@SessionScoped
public class EpisodeBean implements Serializable {

    private static final long serialVersionUID = 1343652914148184382L;

    @Inject
    private ShowService showService;

    private Show show;

    private Episode episode;

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

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public void downloadEpisode() {
        System.out.println("### Download episode: " + episode);
    }
}
