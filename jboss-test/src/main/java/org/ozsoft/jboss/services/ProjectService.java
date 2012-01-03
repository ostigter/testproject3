package org.ozsoft.jboss.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ozsoft.jboss.entities.Project;

@Stateless
public class ProjectService {
    
    @PersistenceContext
    private EntityManager em;
    
    public void store(Project project) {
        if (project.getId() == null) {
            em.persist(project);
        } else {
            em.merge(project);
        }
    }
    
    public List<Project> list() {
        return em.createQuery("SELECT p FROM Project p", Project.class).getResultList();
    }
    
    public Project retrieve(long id) {
        return em.find(Project.class, id);
    }
    
    public void delete(long id) {
    	Project project = retrieve(id);
    	if (project != null) {
    		delete(project);
    	}
    }

    public void delete(Project project) {
        em.remove(project);
    }
    
}
