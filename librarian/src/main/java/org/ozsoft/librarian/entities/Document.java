package org.ozsoft.librarian.entities;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

@Entity
public class Document extends BaseEntity {
    
    private static final long serialVersionUID = 6533376308020034563L;
    
    @Basic
    private String title;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(name = "versionNumber")
    private Map<Integer, DocumentVersion> versions;
    
    public Document() {
        versions = new TreeMap<Integer, DocumentVersion>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void addVersion(DocumentVersion version) {
        versions.put(version.getVersionNumber(), version);
    }
    
    public DocumentVersion getVersion(long versionNumber) {
        return versions.get(versionNumber);
    }

    /* package */ void setVersions(Map<Integer, DocumentVersion> versions) {
        this.versions = versions;
    }

    /* package */ Map<Integer, DocumentVersion> getVersions() {
        return versions;
    }

}
