package org.ozsoft.photobook.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.ozsoft.photobook.entities.BaseEntity;

public abstract class Repository<T extends BaseEntity> {

    private static final Logger LOGGER = Logger.getLogger(Repository.class);

    protected final Class<T> entityClass;

    protected final String className;

    protected final EntityManager em;

    private final Query findAllQuery;

    protected Repository(Class<T> entityClass) {
        this.entityClass = entityClass;
        int p = entityClass.getName().lastIndexOf('.');
        if (p >= 0) {
            className = entityClass.getName().substring(p + 1);
        } else {
            className = entityClass.getName();
        }
        em = PersistenceService.getEntityManager();
        findAllQuery = em.createQuery("SELECT e FROM " + className + " e", entityClass);
    }

    public void store(T entity) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            if (entity.getId() == null) {
                em.persist(entity);
            } else {
                em.merge(entity);
            }
            tx.commit();
        } catch (PersistenceException e) {
            LOGGER.error("Could not store entity into database", e);
            tx.rollback();
        }
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return findAllQuery.getResultList();
    }

    public T retrieveById(long id) {
        return em.find(entityClass, id);
    }

    public void delete(T entity) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(entity);
            tx.commit();
        } catch (PersistenceException e) {
            LOGGER.error("Could not delete entity from database", e);
            tx.rollback();
        }
    }

}
