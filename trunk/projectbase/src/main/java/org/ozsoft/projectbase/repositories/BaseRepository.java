package org.ozsoft.projectbase.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ozsoft.projectbase.entities.BaseEntity;

public abstract class BaseRepository<T extends BaseEntity> implements Repository<T> {
    
    private static final String PU_NAME = "projectbasePU";
    
    private final Class<T> entityClass;
    
    @PersistenceContext(unitName = PU_NAME)
    private EntityManager em;
    
    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }
    
	protected void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	@Override
    public void store(T entity) {
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            em.merge(entity);
        }
    }
    
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findAll() {
		return em.createQuery("SELECT e FROM " + entityClass.getName() + " e").getResultList();
	}
	
	@Override
    public T retrieve(long id) {
        return em.find(entityClass, id);
    }
    
	@Override
    public void delete(T entity) {
        em.remove(entity);
    }

}
