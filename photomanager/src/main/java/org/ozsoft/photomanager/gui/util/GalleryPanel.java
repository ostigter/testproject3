package org.ozsoft.photomanager.gui.util;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.ozsoft.photomanager.gui.UIConstants;

/**
 * Gallery panel for browsing selectable items in a top-left to bottom-right
 * layout.
 * 
 * @author Oscar Stigter
 */
public abstract class GalleryPanel extends JPanel implements GalleryListener {

	private static final long serialVersionUID = 6576478313016045049L;

	private final Set<GalleryItem> items;

	private GalleryItem selectedItem;

	/**
	 * Constructor.
	 */
	public GalleryPanel() {
		items = new HashSet<GalleryItem>();
		setLayout(new GalleryLayout());
		setBorder(new LineBorder(UIConstants.LINE_COLOR));
	}

	/**
	 * Adds an item to the gallery.
	 * 
	 * @param item
	 *            The item.
	 */
	public void addItem(GalleryItem item) {
		add(item);
		items.add(item);
		revalidate();
	}

	@Override
	public void itemSelected(GalleryItem item) {
		if (selectedItem != null) {
			selectedItem.setSelected(false);
		}
		selectedItem = item;
		item.setSelected(true);
	}

}
