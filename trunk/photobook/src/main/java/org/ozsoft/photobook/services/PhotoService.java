package org.ozsoft.photobook.services;

import java.io.InputStream;

import javax.ejb.Local;

import org.ozsoft.photobook.entities.Photo;

@Local
public interface PhotoService {

    long persist(Photo photo);

    Photo retrieve(long id);

    void update(Photo photo);
    
    InputStream getContent(Photo photo);
    
    void setContent(Photo photo, InputStream is);

    void delete(Photo photo);

}
