package org.ozsoft.jboss.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.ozsoft.jboss.entities.Release;

@ManagedBean
@SessionScoped
public class ReleaseBean implements Serializable {

    private static final long serialVersionUID = 8246447601649350345L;

    private String title;

    private String projectName;
    
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
        return new ArrayList<Release>();
    }

    public String add() {
        if (projectName != null && projectName.length() > 0) {
            title = "Add Release";
            release = null;
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
        if (release == null) {
            release = new Release();
//            release.setProject(project);
        }
        release.setName(name);
//        releaseService.store(release);
        return "listReleases.xhtml";
    }

    public String delete() {
        if (release != null) {
//            releaseService.delete(release.getId());
        }
        return "listReleases.xhtml";
    }

    public String cancel() {
        return "listReleases.xhtml";
    }
    
}
