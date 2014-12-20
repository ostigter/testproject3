package org.ozsoft.mediacenter;

public class ShowsMediaPanel extends MediaPanel {

    private static final long serialVersionUID = 5098435366724494781L;

    public ShowsMediaPanel(Configuration config, Library library) {
        super(config, library);
    }

    @Override
    public void libraryUpdated() {
        currentFolder = library.getShowsRoot();
        updateList();
    }
}
