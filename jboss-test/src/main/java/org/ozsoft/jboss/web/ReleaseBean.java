package org.ozsoft.jboss.web;

import java.io.Serializable;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

import org.ozsoft.jboss.entities.Project;
import org.ozsoft.jboss.entities.Release;
import org.ozsoft.jboss.services.ProjectService;

@ManagedBean
@SessionScoped
public class ReleaseBean implements Serializable {

    private static final long serialVersionUID = 8246447601649350345L;

    @EJB
    private ProjectService projectService;

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
    
    public void projectChanged(ValueChangeEvent e) {
        project = projectService.retrieveByName(projectName);
        if (project != null) {
            System.out.format("*** Project changed: '%s'\n", project);
        }
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
        if (release != null) {
            title = "Edit Release";
            name = release.getName();
            return "editRelease.xhtml";
        } else {
            System.out.println("*** Release not set");
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
        }
        return "listReleases.xhtml";
    }

    public String delete() {
        if (release != null) {
            projectService.deleteRelease(release.getId());
        }
        return "listReleases.xhtml";
    }

    public String cancel() {
        return "listReleases.xhtml";
    }

}
