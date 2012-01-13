package org.ozsoft.projectbase.repositories;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;
import org.ozsoft.projectbase.entities.Project;
import org.ozsoft.projectbase.entities.Release;

@ManagedBean(eager = true)
@ApplicationScoped
public class ProjectRepository extends Repository<Project> {

    private static final Logger LOGGER = Logger.getLogger(ProjectRepository.class);

    public ProjectRepository() {
        super(Project.class);
    }
    
    public Release retrieveRelease(long id) {
        return em.find(Release.class, id);
    }
    
    public void deleteRelease(long id) {
        Release release = retrieveRelease(id);
        if (release != null) {
            deleteRelease(release);
        }
    }

    public void deleteRelease(Release release) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(release);
            tx.commit();
        } catch (PersistenceException e) {
            tx.rollback();
            LOGGER.error(String.format("Could not delete release '%s' from database", release), e);
        }
    }
    
}
