package org.ozsoft.mediacenter;

public class MoviesMediaPanel extends MediaPanel {

    private static final long serialVersionUID = -4843396523089981487L;

    public MoviesMediaPanel(Configuration config, Library library) {
        super(config, library);
    }

    @Override
    public void libraryUpdated() {
        currentFolder = library.getMoviesRoot();
        updateList();
    }
}
