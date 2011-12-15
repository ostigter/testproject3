package org.ozsoft.projectbase.web;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ozsoft.projectbase.entities.Project;
import org.ozsoft.projectbase.repositories.ProjectRepository;

@ManagedBean
@RequestScoped
public class ProjectBean implements Serializable {
    
    private static final long serialVersionUID = -8503605436811062379L;
    
    @EJB
    private ProjectRepository projectRepository;
    
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
            projectRepository.store(project);
            long id = project.getId();
            System.out.println("*** Created project with ID " + id);
            project = projectRepository.retrieve(id);
            System.out.println("*** Retrieved project with ID " + project.getId());
        }
    }

}
