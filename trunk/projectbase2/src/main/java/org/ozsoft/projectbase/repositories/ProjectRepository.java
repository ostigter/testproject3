package org.ozsoft.projectbase.repositories;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.ozsoft.projectbase.entities.Project;

@ManagedBean(eager = true)
@ApplicationScoped
public class ProjectRepository extends Repository<Project> {

    public ProjectRepository() {
        super(Project.class);
    }
    
}
