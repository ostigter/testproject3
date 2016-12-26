package org.ozsoft.blackbeard.services;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.ozsoft.blackbeard.domain.Episode;
import org.ozsoft.blackbeard.domain.Show;
import org.ozsoft.blackbeard.domain.ShowStatus;

public class ShowServiceTest {

    @Test
    public void searchShows() {
        ShowService showService = new ShowService();
        List<Show> shows = showService.searchShows("the walking dead");
        Assert.assertTrue(shows.size() > 1);
    }

    @Test
    public void updateEpisodes() {
        ShowService showService = new ShowService();
        Show show = new Show(73, "The Walking Dead", null, ShowStatus.RUNNING);
        showService.updateEpisodes(show);
        Episode[] episodes = show.getEpisodes();
        Assert.assertTrue(episodes.length >= 95);
    }
}
