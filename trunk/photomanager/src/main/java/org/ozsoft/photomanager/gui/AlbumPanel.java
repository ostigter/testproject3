package org.ozsoft.photomanager.gui;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.ozsoft.photomanager.entities.Album;

public class AlbumPanel extends JPanel {

    private static final long serialVersionUID = 6096580313539708996L;
    
    public AlbumPanel() {
        setLayout(new GalleryLayout());
        setBorder(new LineBorder(Color.LIGHT_GRAY));
    }
    
    public void addAlbum(Album album) {
        AlbumIcon albumIcon = new AlbumIcon();
        albumIcon.setAlbum(album);
        add(albumIcon);
        revalidate();
    }

}
