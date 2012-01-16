package org.ozsoft.projectbase.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Release extends BaseEntity {

    private static final long serialVersionUID = -1541127939474309492L;

    @ManyToOne
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}
