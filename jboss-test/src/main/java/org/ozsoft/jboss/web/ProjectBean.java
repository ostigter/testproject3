package org.ozsoft.jboss.web;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ozsoft.jboss.entities.Project;
import org.ozsoft.jboss.services.ProjectService;

@ManagedBean
@RequestScoped
public class ProjectBean implements Serializable {
    
    private static final long serialVersionUID = 4728584552025654369L;

    @EJB
    private ProjectService projectService;
    
    private String title;
    
    private Project project;
    
    private String code;
    
    private String name;
    
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
        return projectService.list();
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
        projectService.store(project);
        return "listProjects.xhtml";
    }
    
    public String delete() {
    	if (project != null) {
    		projectService.delete(project.getId());
    	}
        return "listProjects.xhtml";
    }
    
    public String cancel() {
        return "listProjects.xhtml";
    }

}
