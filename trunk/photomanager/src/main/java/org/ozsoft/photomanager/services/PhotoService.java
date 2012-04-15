package org.ozsoft.photomanager.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.ozsoft.photomanager.entities.Album;
import org.ozsoft.photomanager.entities.Photo;

public class PhotoService {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(PhotoService.class);

    /** Entitiy manager. */
    private EntityManager em;

    /**
     * Constructor.
     */
    public PhotoService() {
        em = PersistenceService.getEntityManager();
    }

    /**
     * Stores a photo.
     * 
     * @param photo
     *            The photo.
     */
    public void storePhoto(Photo photo) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            if (photo.getId() == null) {
                em.persist(photo);
            } else {
                em.merge(photo);
            }
            tx.commit();
            LOGGER.debug("Stored photo with ID " + photo.getId());
        } catch (PersistenceException e) {
            tx.rollback();
            LOGGER.error("Error storing photo", e);
        }
    }

    /**
     * Retrieves a photo by its ID.
     * 
     * @param id
     *            The photo ID.
     * 
     * @return The photo if found, otherwise null.
     */
    public Photo retrievePhoto(long id) {
        return em.find(Photo.class, id);
    }

    /**
     * Stores an album.
     * 
     * @param album
     *            The album.
     */
    public void storeAlbum(Album album) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            if (album.getId() == null) {
                em.persist(album);
            } else {
                em.merge(album);
            }
            tx.commit();
            LOGGER.debug("Stored album with ID " + album.getId());
        } catch (PersistenceException e) {
            tx.rollback();
            LOGGER.error("Error storing album", e);
        }
    }
    
    public List<Album> listAlbums() {
        return em.createQuery("SELECT a FROM Album a", Album.class).getResultList();
    }

    /**
     * Retrieve an album by its ID.
     * 
     * @param id
     *            The album ID.
     * 
     * @return The album if found, otherwise null.
     */
    public Album retrieveAlbum(long id) {
        return em.find(Album.class, id);
    }

}
