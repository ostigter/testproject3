package org.ozsoft.photobook.services;

import java.io.InputStream;

import org.ozsoft.photobook.entities.Photo;

public interface PhotoService {

    void store(Photo photo);

    Photo retrieve(long id);

    InputStream getContent(Photo photo);
    
    void setContent(Photo photo, InputStream is);

    void delete(Photo photo);

}
