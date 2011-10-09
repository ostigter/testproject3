package org.ozsoft.photobook.services;

import java.io.InputStream;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ozsoft.photobook.entities.Photo;

@Stateless
public class PhotoServiceImpl implements PhotoService {

    @PersistenceContext(unitName = "photobookPU")
    private EntityManager em;

    @Override
    public long persist(Photo photo) {
        em.persist(photo);
        return photo.getId();
    }

    @Override
    public Photo retrieve(long id) {
        return em.find(Photo.class, id);
    }

    @Override
    public InputStream getContent(Photo photo) {
        InputStream is = null;
        return is;
    }

    @Override
    public void setContent(Photo photo, InputStream is) {
    }

    @Override
    public void update(Photo photo) {
        em.merge(photo);
    }

    @Override
    public void delete(Photo photo) {
        em.remove(photo);
    }

}
