package org.ozsoft.photobook.dal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Local;

import org.ozsoft.photobook.entities.Photo;

@Local
public interface PhotoDao {
    
    void store(Photo photo);

    Photo retrieve(long id);
    
    List<Photo> findAll();

    InputStream getContent(long id) throws SQLException;
    
    void setContent(long id, InputStream is) throws IOException;

    void delete(Photo photo);

}
