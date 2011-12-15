package org.ozsoft.projectbase.repositories;

import java.util.List;

import javax.ejb.Local;

import org.ozsoft.projectbase.entities.BaseEntity;

@Local
public interface Repository<T extends BaseEntity> {
	
	void store(T entity);
	
	List<T> findAll();
	
	T retrieve(long id);
	
	void delete(T entity);

}
