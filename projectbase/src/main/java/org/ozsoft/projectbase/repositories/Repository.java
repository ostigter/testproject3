package org.ozsoft.projectbase.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ozsoft.projectbase.entities.BaseEntity;

public abstract class Repository<T extends BaseEntity> {

    protected final Class<T> entityClass;

    protected String className;

    @PersistenceContext
    protected EntityManager em;
    
    protected Repository(Class<T> entityClass) {
        this.entityClass = entityClass;
        className = entityClass.getName();
        int p = className.lastIndexOf('.');
        if (p >= 0) {
            className = className.substring(p + 1);
        }
    }

    public void store(T entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }

    public List<T> findAll() {
    	return em.createQuery("SELECT e FROM " + className + " e", entityClass).getResultList();
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
    
    public void delete(long id) {
    	T entity = retrieveById(id);
    	if (entity != null) {
    		delete(entity);
    	}
    }

    public void delete(T entity) {
        em.remove(entity);
    }

}
