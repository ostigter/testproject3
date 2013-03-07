package org.ozsoft.photomanager.gui;

import org.ozsoft.photomanager.entities.Album;

public interface AlbumListener {
    
    void albumSelected(Album album);
    
    void albumOpened(Album album);

}
