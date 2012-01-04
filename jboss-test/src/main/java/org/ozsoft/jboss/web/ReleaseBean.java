package org.ozsoft.jboss.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.ozsoft.jboss.entities.Project;
import org.ozsoft.jboss.entities.Release;

@ManagedBean
@RequestScoped
public class ReleaseBean implements Serializable {

	private static final long serialVersionUID = 8246447601649350345L;
	
	private Project project;
	
	private Release release;

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
	
	public List<Release> getReleases() {
		return new ArrayList<Release>();
	}

	public void add() {
	}

	public void edit() {
	}

}
