package org.ozsoft.projectbase.web;

import java.io.Serializable;
import java.util.List;

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

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public String addProject() {
        return "addProject.xhtml";
    }

    public String save() {
        Project project = new Project();
        project.setName(name);
        projectRepository.store(project);
        return "index.xhtml";
    }

}
