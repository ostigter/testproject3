package org.ozsoft.projectbase.web;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.ozsoft.projectbase.entities.Project;
import org.ozsoft.projectbase.entities.Release;
import org.ozsoft.projectbase.entities.VersionNumber;
import org.ozsoft.projectbase.repositories.ProjectRepository;

@ManagedBean
@SessionScoped
public class ReleaseBean implements Serializable {

    private static final long serialVersionUID = 8246447601649350345L;

    @ManagedProperty(value = "#{projectRepository}")
    private ProjectRepository projectRepository;

    private String title;

    private String projectName;

    private Project project;

    private Release release;

    private String versionNumber;

    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

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

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public List<Release> getReleases() {
        List<Release> releases = null;
        if (projectName != null && projectName.length() > 0) {
            project = projectRepository.retrieveByName(projectName);
            if (project != null) {
                releases = project.getReleases();
            }
        }
        return releases;
    }

    public String add() {
        if (projectName != null && projectName.length() > 0) {
            project = projectRepository.retrieveByName(projectName);
            title = "Add Release";
            release = null;
            versionNumber = "";
            return "editRelease.xhtml";
        } else {
            return "listReleases.xhtml";
        }
    }

    public String edit() {
        if (release != null) {
            title = "Edit Release";
            versionNumber = release.getVersionNumber().toString();
            return "editRelease.xhtml";
        } else {
            return "listReleases.xhtml";
        }
    }

    public String save() {
        VersionNumber vn = null;
        try {
            vn = VersionNumber.parse(versionNumber);
            System.out.format("Version number: '%s'\n", vn);
            if (release == null) {
                release = new Release();
                release.setProject(project);
                project.getReleases().add(release);
            }
            release.setVersionNumber(vn);
            projectRepository.store(project);
        } catch (ParseException e) {
            // TODO: Handle invalid version number
            System.out.println(e);
        }
        return "listReleases.xhtml";
    }

    public String delete() {
        if (release != null) {
            project.getReleases().remove(release);
            projectRepository.store(project);
        }
        return "listReleases.xhtml";
    }

    public String cancel() {
        return "listReleases.xhtml";
    }

}
