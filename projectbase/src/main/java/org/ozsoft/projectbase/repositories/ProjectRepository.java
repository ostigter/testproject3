package org.ozsoft.projectbase.repositories;

import javax.ejb.Stateless;

import org.ozsoft.projectbase.entities.Project;

@Stateless
public class ProjectRepository extends Repository<Project> {

    public ProjectRepository() {
        super(Project.class);
    }

}
