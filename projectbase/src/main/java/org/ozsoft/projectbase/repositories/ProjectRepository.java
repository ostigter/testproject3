package org.ozsoft.projectbase.repositories;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ozsoft.projectbase.entities.Project;

@Singleton
public class ProjectRepository {
    
    private static final String PU_NAME = "projectbasePU";
    
    @PersistenceContext(unitName = PU_NAME)
    private EntityManager em;
    
    public void store(Project project) {
        if (project.getId() == null) {
            em.persist(project);
        } else {
            em.merge(project);
        }
        System.out.println("*** Stored project with ID " + project.getId());
    }
    
}
