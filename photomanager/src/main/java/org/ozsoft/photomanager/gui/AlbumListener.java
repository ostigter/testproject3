package org.ozsoft.photomanager.gui;

import org.ozsoft.photomanager.entities.Album;

/**
 * Listener for album events.
 * 
 * @author Oscar Stigter
 */
public interface AlbumListener {

	/**
	 * Event indicating that an album has been selected.
	 * 
	 * @param album
	 *            The selected album.
	 */
	void albumSelected(Album album);

	/**
	 * Event indicating that an album has been opened.
	 * 
	 * @param album
	 *            The opened album.
	 */
	void albumOpened(Album album);

}
