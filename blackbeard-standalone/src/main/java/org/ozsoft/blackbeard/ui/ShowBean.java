package org.ozsoft.blackbeard.ui;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.services.ShowService;

@Named
@RequestScoped
public class ShowBean {

    @Inject
    private ShowService showService;

    public Show[] getShows() {
        return showService.getShows();
    }
}
