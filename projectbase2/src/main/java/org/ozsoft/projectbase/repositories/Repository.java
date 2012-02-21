package org.ozsoft.projectbase.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.ozsoft.projectbase.entities.BaseEntity;

public abstract class Repository<T extends BaseEntity> {

    private static final Logger LOGGER = Logger.getLogger(Repository.class);

    protected final Class<T> entityClass;

    protected String className;

    protected EntityManager em;

    protected Repository(Class<T> entityClass) {
        this.entityClass = entityClass;
        className = entityClass.getName();
        int p = className.lastIndexOf('.');
        if (p >= 0) {
            className = className.substring(p + 1);
        }
        em = PersistenceService.getEntityManager();
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

    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + className + " e ORDER BY e.name", entityClass).getResultList();
    }

    public T retrieveById(long id) {
        return em.find(entityClass, id);
    }

    @SuppressWarnings("unchecked")
    public T retrieveByName(String name) {
        Query query = em.createQuery("SELECT e FROM " + className + " e WHERE e.name = :name", entityClass);
        query.setParameter("name", name);
        List<T> entities = query.getResultList();
        return (entities.size() > 0) ? entities.get(0) : null;
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
