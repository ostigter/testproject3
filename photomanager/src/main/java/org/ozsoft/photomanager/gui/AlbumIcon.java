package org.ozsoft.photomanager.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import org.ozsoft.photomanager.entities.Album;
import org.ozsoft.photomanager.entities.Photo;
import org.ozsoft.photomanager.gui.util.GalleryItem;

/**
 * A single album icon as listed in the {@link AlbumPanel}.
 * 
 * @author Oscar Stigter
 */
@SuppressWarnings("rawtypes")
public class AlbumIcon extends GalleryItem {

    private static final long serialVersionUID = -3913203912804081255L;

    private static final String DEFAULT_ALBUM_IMAGE = "/images/album.png";

    private final Album album;

    private final JLabel thumbnailLabel;

    private final JLabel nameLabel;

    private final JLabel dateLabel;

    /**
     * Constructor.
     * 
     * @param album
     *            The associated album.
     * @param listener
     *            The gallery listener.
     */
    @SuppressWarnings("unchecked")
    public AlbumIcon(Album album, AlbumPanel parent) {
        super(parent);

        this.album = album;

        setBackground(UIConstants.DEFAULT_BACKGROUND_COLOR);
        setBorder(new LineBorder(UIConstants.LINE_COLOR));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Thumbnail of album's cover photo.
        thumbnailLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        add(thumbnailLabel, gbc);

        nameLabel = new JLabel();
        nameLabel.setFont(UIConstants.NORMAL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 0, 5);
        add(nameLabel, gbc);

        dateLabel = new JLabel();
        dateLabel.setFont(UIConstants.SMALL_FONT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 5, 5);
        add(dateLabel, gbc);

        Photo coverPhoto = album.getCoverPhoto();
        if (coverPhoto != null) {
            thumbnailLabel.setIcon(new ImageIcon(coverPhoto.getThumbnail()));
        } else {
            // No cover photo set; use placeholder icon.
            thumbnailLabel.setIcon(new ImageIcon(getClass().getResource(DEFAULT_ALBUM_IMAGE)));
        }

        String name = album.getName();
        if (name != null) {
            nameLabel.setText(name);
        }

        dateLabel.setText(UIConstants.DATE_FORMAT.format(album.getDate()));
    }

    /**
     * Returns the associated album.
     * 
     * @return The album.
     */
    public Album getAlbum() {
        return album;
    }

    @Override
    protected void doSelected() {
        setBackground(UIConstants.SELECTION_BACKGROUND_COLOR);
    }

    @Override
    protected void doUnselected() {
        setBackground(UIConstants.DEFAULT_BACKGROUND_COLOR);
    }

    @Override
    public String toString() {
        return album.getName();
    }

}
