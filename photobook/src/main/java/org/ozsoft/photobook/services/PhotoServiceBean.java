package org.ozsoft.photobook.services;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import org.ozsoft.photobook.dal.PhotoDao;
import org.ozsoft.photobook.entities.Photo;

@Singleton
public class PhotoServiceBean implements PhotoService {
    
    @EJB
    private PhotoDao photoDao;
    
    @Override
    public void store(Photo photo) {
        photoDao.store(photo);
    }

    @Override
    public List<Photo> findAll() {
        return photoDao.findAll();
    }

    @Override
    public Photo retrieve(long id) {
        return photoDao.retrieve(id);
    }

    @Override
    public InputStream getContent(long id) throws SQLException {
        return photoDao.getContent(id);
    }

    @Override
    public void setContent(long id, InputStream is) throws IOException {
        photoDao.setContent(id, is);
    }

    @Override
    public void delete(Photo photo) {
        photoDao.delete(photo);
    }

}
