package org.ozsoft.jboss.services;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    
    @SuppressWarnings("unchecked")
	public Project retrieveByName(String name) {
    	Project project = null;
    	Query query = em.createQuery("SELECT p FROM Project p WHERE p.name = :name", Project.class);
    	query.setParameter("name", name);
    	List<Project> projects = query.getResultList();
    	if (projects.size() > 0) {
    		project = projects.get(0);
    	}
    	return project;
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
