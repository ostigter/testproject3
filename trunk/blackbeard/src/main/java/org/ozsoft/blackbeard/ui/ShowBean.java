package org.ozsoft.blackbeard.ui;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.services.ShowService;

@ManagedBean
@RequestScoped
public class ShowBean implements Serializable {

    private static final long serialVersionUID = 2413353027667909815L;

    @ManagedProperty(value = "#{showService}")
    private ShowService showService;

    public void setShowService(ShowService showService) {
        this.showService = showService;
    }

    public Show[] getShows() {
        return showService.getShows();
    }
}
