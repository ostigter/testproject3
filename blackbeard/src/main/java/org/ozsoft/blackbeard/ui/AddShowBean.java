package org.ozsoft.blackbeard.ui;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.services.ShowService;

@ManagedBean
@RequestScoped
public class AddShowBean implements Serializable {

    private static final long serialVersionUID = -897134941768788958L;

    @ManagedProperty(value = "#{showService}")
    private ShowService showService;

    private String name;

    private List<Show> matchingShows;

    private Show selectedShow;

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

    public Show getSelectedShow() {
        return selectedShow;
    }

    public void setSelectedShow(Show show) {
        this.selectedShow = show;
    }
}
