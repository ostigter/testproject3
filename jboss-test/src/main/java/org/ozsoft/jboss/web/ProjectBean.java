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
    
    private String code;
    
    private String name;

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
    
    public String doAdd() {
        if (code.length() > 0) {
            Project project = new Project();
            project.setCode(code);
            project.setName(name);
            projectService.store(project);
            return "listProjects.xhtml";
        } else {
            return null;
        }
    }
    
    public String cancel() {
        return "listProjects.xhtml";
    }

}
