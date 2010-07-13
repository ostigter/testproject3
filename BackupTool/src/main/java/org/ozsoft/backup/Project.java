package org.ozsoft.backup;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Project {
    
    private final String name;
    
    private final Set<String> sourceFolders;
    
    private String destinationFolder;
    
    private final Map<Integer, Date> backups;
    
    private int nextBackupId;
    
    public Project(String name) {
        this.name = name;
        sourceFolders = new TreeSet<String>();
        backups = new TreeMap<Integer, Date>();
        nextBackupId = 0;
    }
    
    public String getName() {
        return name;
    }
    
    public Set<String> getSourceFolders() {
        return Collections.unmodifiableSet(sourceFolders);
    }
    
    public void addSourceFolder(String sourceFolder) {
        sourceFolders.add(sourceFolder);
    }
    
    public void removeSourceFolder(String sourceFolder) {
        sourceFolders.remove(sourceFolder);
    }
    
    public String getDestinationFolder() {
        return destinationFolder;
    }
    
    public void setDestinationFolder(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }
    
    public void createBackup() {
        int id = nextBackupId++;
        Date date = new Date();
        for (String sourceFolder : sourceFolders) {
            
        }
        backups.put(id, date);
    }
    
}
