package org.ozsoft.projectbase.repositories;

import javax.ejb.Local;

import org.ozsoft.projectbase.entities.Project;

@Local
public interface ProjectDao {
    
    void store(Project project);

}
