package org.ozsoft.projectbase.web;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.ozsoft.projectbase.entities.Project;
import org.ozsoft.projectbase.repositories.ProjectRepository;

@ManagedBean
@SessionScoped
public class ProjectBean implements Serializable {

    private static final long serialVersionUID = 4728584552025654369L;

    @ManagedProperty(value = "#{projectRepository}")
    private ProjectRepository projectRepository;
    
    private String title;

    private Project project;

    private String code;

    private String name;
    
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public String getTitle() {
        return title;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public String add() {
        title = "Add Project";
        project = null;
        code = "";
        name = "";
        return "editProject.xhtml";
    }

    public String edit() {
        title = "Edit Project";
        code = project.getCode();
        name = project.getName();
        return "editProject.xhtml";
    }

    public String save() {
        if (project == null) {
            project = new Project();
        }
        project.setCode(code);
        project.setName(name);
        projectRepository.store(project);
        return "listProjects.xhtml";
    }

    public String delete() {
        if (project != null) {
            projectRepository.delete(project);
        }
        return "listProjects.xhtml";
    }

    public String cancel() {
        return "listProjects.xhtml";
    }
    
}
