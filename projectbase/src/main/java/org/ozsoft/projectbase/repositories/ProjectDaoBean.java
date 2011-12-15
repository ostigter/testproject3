package org.ozsoft.projectbase.repositories;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ozsoft.projectbase.entities.Project;

@Stateless
public class ProjectDaoBean implements ProjectDao {

    @PersistenceContext(unitName = "projectbasePU")
    private EntityManager em;
    
    @Override
    public void store(Project project) {
        em.persist(project);
    }

}
