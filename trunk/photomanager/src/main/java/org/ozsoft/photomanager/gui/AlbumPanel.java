package org.ozsoft.photomanager.gui;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.ozsoft.photomanager.entities.Album;

public class AlbumPanel extends JPanel implements AlbumListener {

    private static final long serialVersionUID = 6096580313539708996L;
    
    private final Set<AlbumIcon> albumIcons;
    
    public AlbumPanel() {
        albumIcons = new HashSet<AlbumIcon>();
        setLayout(new GalleryLayout());
        setBorder(new LineBorder(Color.LIGHT_GRAY));
    }
    
    public void addAlbum(Album album) {
        AlbumIcon albumIcon = new AlbumIcon(album, this);
        add(albumIcon);
        albumIcons.add(albumIcon);
        revalidate();
    }

    @Override
    public void albumSelected(Album album) {
        System.out.format("### Album selected: '%s'\n", album);
        for (AlbumIcon albumIcon : albumIcons) {
            albumIcon.unselect();
        }
    }

    @Override
    public void albumOpened(Album album) {
        System.out.format("### Album opened: '%s'\n", album);
    }

}
