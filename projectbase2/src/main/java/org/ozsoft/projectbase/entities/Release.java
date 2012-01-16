package org.ozsoft.projectbase.entities;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Release extends BaseEntity {

    private static final long serialVersionUID = -1541127939474309492L;
    
    @ManyToOne
    private Project project;

    @Embedded
    private VersionNumber versionNumber;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public VersionNumber getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(VersionNumber versionNumber) {
        this.versionNumber = versionNumber;
    }
    
    @Override
    public String toString() {
        return String.format("%s %s", project, versionNumber);
    }

}
