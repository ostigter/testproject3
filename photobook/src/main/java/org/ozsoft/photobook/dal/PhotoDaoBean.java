package org.ozsoft.photobook.dal;

import java.io.InputStream;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ozsoft.photobook.entities.Photo;

@Stateless
@LocalBean
public class PhotoDaoBean implements PhotoDao {

    @PersistenceContext(unitName = "photobookPU")
    private EntityManager em;

    @Override
    public void store(Photo photo) {
        if (photo.getId() == null) {
            em.persist(photo);
        } else {
            em.merge(photo);
        }
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
    public void delete(Photo photo) {
        em.remove(photo);
    }

}
