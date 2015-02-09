package org.ozsoft.blackbeard.ui;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.services.ShowService;

@Named
@RequestScoped
public class ShowBean implements Serializable {

    private static final long serialVersionUID = 2413353027667909815L;

    @Inject
    private ShowService showService;

    public void setShowService(ShowService showService) {
        this.showService = showService;
    }

    public Show[] getShows() {
        return showService.getShows();
    }
}
