package org.ozsoft.projectbase.repositories;

import javax.ejb.Stateless;

import org.ozsoft.projectbase.entities.Project;
import org.ozsoft.projectbase.entities.Release;

@Stateless
public class ProjectRepository extends Repository<Project> {

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
        em.remove(release);
    }
    
}
