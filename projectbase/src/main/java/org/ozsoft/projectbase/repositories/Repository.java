package org.ozsoft.projectbase.repositories;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ozsoft.projectbase.entities.BaseEntity;

public abstract class Repository<T extends BaseEntity> {
    
    private static final String PU_NAME = "projectbasePU";
    
    private final Class<T> entityClass;
    
    @PersistenceContext(unitName = PU_NAME)
    private EntityManager em;
    
    @SuppressWarnings("unchecked")
    public Repository() {
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
    
    public Repository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
    public void store(T entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
    public T retrieve(long id) {
        return em.find(entityClass, id);
    }
    
    public void delete(T entity) {
        em.remove(entity);
    }

}
