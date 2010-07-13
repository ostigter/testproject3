package org.ozsoft.backup;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
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
    
    private final Map<Integer, Long> backups;
    
    private final Map<String, BackupFile> files;
    
    public Project(String name) {
        this.name = name;
        sourceFolders = new TreeSet<String>();
        backups = new TreeMap<Integer, Long>();
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
    
    public String getDestinationFolder() {
        return destinationFolder;
    }
    
    public void setDestinationFolder(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }
    
    public void createBackup() {
        if (sourceFolders.size() == 0) {
            throw new IllegalStateException("No source folders defined");
        }
        if (destinationFolder == null || destinationFolder.length() == 0) {
            throw new IllegalStateException("Destination folder not set");
        }
        
        readIndexFile();
        
        int backupId = backups.size() + 1;
        long date = new Date().getTime();
        System.out.println("Creating backup with ID " + backupId);
        
        for (String sourceFolder : sourceFolders) {
            File dir = new File(sourceFolder);
            if (dir.isDirectory()) {
                backupFolder(dir, backupId);
            } else {
                System.err.println("ERROR: Source folder not found: " + sourceFolder);
            }
        }
        backups.put(backupId, date);
        
        writeIndexFile();
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
    
    private void readIndexFile() {
        backups.clear();
        files.clear();
        File file = new File(String.format("%s/%s.idx", destinationFolder, name));
        if (file.isFile()) {
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(file));
                int nofBackups = dis.readInt();
                for (int i = 0; i < nofBackups; i++) {
                    int id = dis.readInt();
                    long date = dis.readLong();
                    backups.put(id, date);
                }
                int nofFiles = dis.readInt();
                for (int i = 0; i < nofFiles; i++) {
                    String path = dis.readUTF();
                    BackupFile backupFile = new BackupFile(path);
                    int nofVersions = dis.readInt();
                    for (int j = 0; j < nofVersions; j++) {
                        int backupId = dis.readInt();
                        long date = dis.readLong();
                        backupFile.addVersion(backupId, date);
                    }
                    files.put(path, backupFile);
                }
            } catch (IOException e) {
                System.err.println("ERROR: " + e.getMessage());
            } finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e) {
                        // Best effort; ignore.
                    }
                }
            }
        }
    }
    
    private void writeIndexFile() {
        File dir = new File(destinationFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(String.format("%s/%s.idx", destinationFolder, name));
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(file));
            dos.writeInt(backups.size());
            for (int backupId : backups.keySet()) {
                long date = backups.get(backupId);
                dos.writeInt(backupId);
                dos.writeLong(date);
            }
            dos.writeInt(files.size());
            for (BackupFile backupFile : files.values()) {
                dos.writeUTF(backupFile.getPath());
                Collection<BackupFileVersion> versions = backupFile.getVersions();
                int nofVersions = versions.size();
                dos.writeInt(nofVersions);
                for (BackupFileVersion version : versions) {
                    dos.writeInt(version.getBackupId());
                    dos.writeLong(version.getDate());
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    // Best effort; ignore.
                }
            }
        }
        
    }
    
}
