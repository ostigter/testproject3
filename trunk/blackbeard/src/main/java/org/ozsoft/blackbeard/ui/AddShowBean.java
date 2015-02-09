package org.ozsoft.blackbeard.ui;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.services.ShowService;

@ManagedBean
@ViewScoped
public class AddShowBean implements Serializable {

    private static final long serialVersionUID = 307051394346077352L;

    @ManagedProperty(value = "#{showService}")
    private ShowService showService;

    private String name;

    private List<Show> matchingShows;

    private int selectedShowId;

    public void setShowService(ShowService showService) {
        this.showService = showService;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void search() {
        if (name != null && name.length() > 1) {
            matchingShows = showService.searchShows(name);
        }
    }

    public Collection<Show> getShows() {
        return matchingShows;
    }

    public int getSelectedShowId() {
        return selectedShowId;
    }

    public void setSelectedShowId(int showId) {
        this.selectedShowId = showId;
    }

    public String add() {
        for (Show matchingShow : matchingShows) {
            if (matchingShow.getId() == selectedShowId) {
                showService.addShow(matchingShow);
                break;
            }
        }
        return "listShows";
    }
}
