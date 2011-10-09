package org.ozsoft.photobook.services;

import java.io.InputStream;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.ozsoft.photobook.dal.PhotoDao;
import org.ozsoft.photobook.entities.Photo;

@Stateless
@LocalBean
public class PhotoServiceBean implements PhotoService {

    @EJB
    private PhotoDao photoDao;
    
    @Override
    public void store(Photo photo) {
        photoDao.store(photo);
    }

    @Override
    public Photo retrieve(long id) {
        return photoDao.retrieve(id);
    }

    @Override
    public InputStream getContent(Photo photo) {
        return photoDao.getContent(photo);
    }

    @Override
    public void setContent(Photo photo, InputStream is) {
        photoDao.setContent(photo, is);
    }

    @Override
    public void delete(Photo photo) {
        photoDao.delete(photo);
    }

}
