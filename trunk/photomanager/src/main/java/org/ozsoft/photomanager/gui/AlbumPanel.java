package org.ozsoft.photomanager.gui;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.ozsoft.photomanager.entities.Album;

/**
 * Panel to browse the albums.
 * 
 * @author Oscar Stigter
 */
public class AlbumPanel extends JPanel implements AlbumListener {

	private static final long serialVersionUID = 6096580313539708996L;

	private final Map<Album, AlbumIcon> albumIcons;

	private Album selectedAlbum;

	/**
	 * Constructor.
	 */
	public AlbumPanel() {
		albumIcons = new HashMap<Album, AlbumIcon>();
		setLayout(new GalleryLayout());
		setBorder(new LineBorder(Color.LIGHT_GRAY));
	}

	/**
	 * Adds an album.
	 * 
	 * @param album
	 *            The album.
	 */
	public void addAlbum(Album album) {
		AlbumIcon albumIcon = new AlbumIcon(album, this);
		add(albumIcon);
		albumIcons.put(album, albumIcon);
		revalidate();
	}

	@Override
	public void albumSelected(Album album) {
		if (selectedAlbum != null) {
			albumIcons.get(selectedAlbum).setSelected(false);
		}
		selectedAlbum = album;
		albumIcons.get(selectedAlbum).setSelected(true);
	}

	@Override
	public void albumOpened(Album album) {
		// TODO
	}

}
