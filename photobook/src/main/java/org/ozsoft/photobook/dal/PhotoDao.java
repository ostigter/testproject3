package org.ozsoft.photobook.dal;

import java.io.InputStream;

import org.ozsoft.photobook.entities.Photo;

public interface PhotoDao {
    
    void store(Photo photo);

    Photo retrieve(long id);

    InputStream getContent(Photo photo);
    
    void setContent(Photo photo, InputStream is);

    void delete(Photo photo);

}
