package org.ozsoft.backup;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Project {
    
    private final String name;
    
    private final Set<String> sourceFolders;
    
    private final Set<String> destinationFolders;
    
    private final List<Date> backups;
    
    private final Map<String, BackupFile> files;
    
    public Project(String name) {
        this.name = name;
        sourceFolders = new TreeSet<String>();
        destinationFolders = new TreeSet<String>();
        backups = new ArrayList<Date>();
        files = new TreeMap<String, BackupFile>();
    }
    
    public String getName() {
        return name;
    }
    
    public Set<String> getSourceFolders() {
        return Collections.unmodifiableSet(sourceFolders);
    }
    
    public void addSourceFolder(String folder) {
        sourceFolders.add(folder);
    }
    
    public void removeSourceFolder(String folder) {
        sourceFolders.remove(folder);
    }
    
    public Set<String> getDestinationFolders() {
        return Collections.unmodifiableSet(destinationFolders);
    }
    
    public void addDestinationFolder(String folder) {
        destinationFolders.add(folder);
    }
    
    public void removeDestinationFolder(String folder) {
        destinationFolders.remove(folder);
    }
    
    public void createBackup() {
        if (sourceFolders.size() == 0) {
            throw new IllegalStateException("Project has no source folders defined");
        }
        if (destinationFolders.size() == 0) {
            throw new IllegalStateException("Project has no destination folders defined");
        }
        int backupId = backups.size() + 1;
        Date date = new Date();
        System.out.println("Creating backup with ID " + backupId);
        for (String sourceFolder : sourceFolders) {
            File dir = new File(sourceFolder);
            if (dir.isDirectory()) {
                backupFolder(dir, backupId);
            } else {
                System.err.println("ERROR: Source folder not found: " + sourceFolder);
            }
        }
        backups.add(date);
    }
    
    private void backupFolder(File dir, int backupId) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                backupFolder(file, backupId);
            } else {
                backupFile(file, backupId);
            }
        }
    }
    
    private void backupFile(File file, int backupId) {
        String path = file.getAbsolutePath();
        long date = file.lastModified();
        BackupFile backupFile = files.get(path);
        if (backupFile == null) {
            // New file; create first version.
            backupFile = new BackupFile(path);
            backupFile.addVersion(backupId, date);
            files.put(path, backupFile);
            System.out.println("A " + path);
        } else {
            // Existing file.
            BackupFileVersion version = backupFile.getCurrentVersion();
            if (version.getDate() == date) {
                // Unchanged file; do nothing.
            } else {
                // Updated file; add new version.
                backupFile.addVersion(backupId, date);
                System.out.println("U " + path);
            }
        }
    }
    
}
