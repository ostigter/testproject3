package org.ozsoft.photomanager.gui;

import org.ozsoft.photomanager.entities.Album;
import org.ozsoft.photomanager.gui.util.GalleryItem;
import org.ozsoft.photomanager.gui.util.GalleryPanel;

/**
 * Panel to browse the albums.
 * 
 * @author Oscar Stigter
 */
public class AlbumPanel extends GalleryPanel {

	private static final long serialVersionUID = -2042112588233826274L;

	/**
	 * Adds an album.
	 * 
	 * @param album
	 *            The album.
	 */
	public void addAlbum(Album album) {
		AlbumIcon albumIcon = new AlbumIcon(album, this);
		addItem(albumIcon);
	}

	@Override
	public void itemOpened(GalleryItem item) {
		// TODO: Open album.
	}

}
