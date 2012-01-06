package org.ozsoft.jboss.web;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.ozsoft.jboss.entities.Project;
import org.ozsoft.jboss.entities.Release;
import org.ozsoft.jboss.services.ProjectService;

@ManagedBean
@SessionScoped
public class ReleaseBean implements Serializable {

    private static final long serialVersionUID = 8246447601649350345L;
    
    @EJB
    private ProjectService projectService;
    
//    @ManagedProperty(value = "#{releaseBean}")
//    private ReleaseBean releaseBean;

    private String title;

    private String projectName;
    
    private Project project;
    
    private Release release;
    
    private String name;

    public String getTitle() {
        return title;
    }

   public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public List<Release> getReleases() {
    	List<Release> releases = null;
        if (projectName != null && projectName.length() > 0) {
            project = projectService.retrieveByName(projectName);
            if (project != null) {
            	releases = project.getReleases();
            }
        }
        return releases;
    }

    public String add() {
        if (projectName != null && projectName.length() > 0) {
            title = "Add Release";
            release = new Release();
            name = "";
            return "editRelease.xhtml";
        } else {
            System.out.println("*** Project not set");
            return "listReleases.xhtml";
        }
    }
    
    public String edit() {
        if (projectName != null && projectName.length() > 0) {
            title = "Edit Release";
            name = release.getName();
            return "editRelease.xhtml";
        } else {
            System.out.println("*** Project not set");
            return "listReleases.xhtml";
        }
    }

    public String save() {
        if (release != null) {
            release.setName(name);
            project = projectService.retrieveByName(projectName);
            release.setProject(project);
            project.getReleases().add(release);
            projectService.store(project);
            System.out.format("Release '%s' of project '%s' stored\n", release, project);
        }
        return "listReleases.xhtml";
    }

    public String delete() {
        if (release != null) {
        	release.setProject(null);
            project = projectService.retrieveByName(projectName);
        	project.getReleases().remove(release);
        	projectService.store(project);
        }
        return "listReleases.xhtml";
    }

    public String cancel() {
        return "listReleases.xhtml";
    }

}
