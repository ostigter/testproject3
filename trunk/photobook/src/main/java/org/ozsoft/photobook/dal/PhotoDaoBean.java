package org.ozsoft.photobook.dal;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.ozsoft.photobook.entities.Photo;

@Singleton
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
    public List<Photo> findAll() {
        return em.createNamedQuery("Photo.findAll", Photo.class).getResultList();
    }

    @Override
    public InputStream getContent(long id) throws SQLException {
        InputStream is = null;
        Photo photo = retrieve(id);
        if (photo != null) {
            Session session = em.unwrap(Session.class);
            session.refresh(photo);
            Blob blob = photo.getContent();
            if (blob != null) {
                is = blob.getBinaryStream();
            }
        }
        return is;
    }

    @Override
    public void setContent(long id, InputStream is) throws IOException {
        Photo photo = retrieve(id);
        if (photo != null) {
            Session session = em.unwrap(Session.class);
            session.refresh(photo);
            Blob blob = session.getLobHelper().createBlob(is, is.available());
            photo.setContent(blob);
            store(photo);
        }
    }

    @Override
    public void delete(Photo photo) {
        em.remove(photo);
    }

}
