package org.ozsoft.photobook.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.ozsoft.photobook.entities.Photo;

public class PhotoServiceImpl implements PhotoService {

    private final EntityManager em;

    public PhotoServiceImpl() {
        em = PersistenceService.getEntityManager();
    }

    @Override
    public long create(Photo photo) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(photo);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            throw e;
        }
        return photo.getId();
    }

    @Override
    public Photo retrieve(long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(Photo photo) {
        // TODO Auto-generated method stub

    }

    @Override
    public void delete(Photo photo) {
        // TODO Auto-generated method stub

    }

}
