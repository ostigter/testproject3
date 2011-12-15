package org.ozsoft.projectbase.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.ozsoft.projectbase.entities.Project;
import org.ozsoft.projectbase.repositories.ProjectDao;

@ManagedBean
@SessionScoped
public class ProjectBean implements Serializable {
    
    private static final long serialVersionUID = -8503605436811062379L;
    
    @EJB
    private ProjectDao projectDao;
    
    private String name;

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void create() {
        if (name.length() > 0) {
            Project project = new Project();
            project.setName(name);
            System.out.println("*** Storing project");
            projectDao.store(project);
            long id = project.getId();
            System.out.println("*** Created project with ID " + id);
//            project = repository.retrieve(id);
//            System.out.println("*** Retrieved project with ID " + project.getId());
        }
    }

}
