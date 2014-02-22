package org.ozsoft.photomanager.gui.util;

/**
 * Listener for gallery item events.
 * 
 * @author Oscar Stigter
 */
public interface GalleryListener {

	/**
	 * Event indicating that an item has been selected.
	 * 
	 * @param item
	 *            The selected item.
	 */
	void itemSelected(GalleryItem item);

	/**
	 * Event indicating that an item has been opened.
	 * 
	 * @param item
	 *            The opened item.
	 */
	void itemOpened(GalleryItem item);

}
