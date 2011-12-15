package org.ozsoft.projectbase.repositories;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.ozsoft.projectbase.entities.BaseEntity;

public abstract class Repository<T extends BaseEntity> {
    
    private static final String PU_NAME = "projectbasePU";
    
    private final Class<T> entityClass;
    
    private Query FIND_ALL_QUERY;
    
    @PersistenceContext(unitName = PU_NAME)
    private EntityManager em;
    
    protected Repository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    @PostConstruct
    protected void postConstruct() {
    	String className = entityClass.getName();
    	int p = className.lastIndexOf('.');
    	if (p >= 0) {
    		className = className.substring(p + 1);
    	}
        FIND_ALL_QUERY = em.createQuery("SELECT e FROM " + className + " e", entityClass);
    }
    
    public void store(T entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return FIND_ALL_QUERY.getResultList();
	}
	
    public T retrieve(long id) {
        return em.find(entityClass, id);
    }
    
    public void delete(T entity) {
        em.remove(entity);
    }

}
