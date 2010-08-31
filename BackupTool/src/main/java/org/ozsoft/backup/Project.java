package org.ozsoft.backup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Backup project specifying the source and destination folders. <br />
 * <br />
 * 
 * Implements the actual creating and restoring of backups.
 * 
 * @author Oscar Stigter
 */
public class Project {
    
    private static final int BUFFER_SIZE = 8192;
    
    private final String name;
    
    private final Set<String> sourceFolders;
    
    private String destinationFolder;
    
    private final Map<Integer, Long> backups;
    
    private final Map<String, BackupFile> files;
    
    private RandomAccessFile archiveFile;
    
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
    
    public Map<Integer, Long> getBackups() {
        return Collections.unmodifiableMap(backups);
    }
    
    public Map<String, BackupFile> getBackupFiles() {
        return Collections.unmodifiableMap(files);
    }
    
    public void createBackup() {
        if (sourceFolders.size() == 0) {
            throw new IllegalStateException("No source folders defined");
        }
        if (destinationFolder == null || destinationFolder.length() == 0) {
            throw new IllegalStateException("Destination folder not set");
        }
        
        readIndexFile();
        
        // Make sure destination folder exists. 
        File dir = new File(destinationFolder);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new RuntimeException("Could not create destination folder: " + destinationFolder);
            }
        }
        
        try {
            int backupId = backups.size() + 1;
            long date = new Date().getTime();
            System.out.println("Creating backup with ID " + backupId);
            backups.put(backupId, date);
            
            openArchiveFile();
            
            // Backup new and updated files.
            for (String sourceFolder : sourceFolders) {
                dir = new File(sourceFolder);
                if (dir.isDirectory()) {
                    backupFolder(dir, backupId);
                } else {
                    System.err.println("ERROR: Source folder not found: " + sourceFolder);
                }
            }
            
            // Flag deleted files.
            for (BackupFile file : files.values()) {
                String path = file.getPath();
                if (!(new File(path).isFile())) {
                    file.addVersion(backupId, date, -1L, 0L);
                    System.out.println("D " + path);
                }
            }
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
            //TODO: Clean up failed backup.
        }

        closeArchiveFile();
        
        writeIndexFile();
    }
    
    public void restoreFile(String path, int backupId) throws IOException {
        BackupFile backupFile = files.get(path);
        if (backupFile == null) {
            throw new IllegalArgumentException(String.format("Backup file not found: '%s'", path));
        }
        BackupFileVersion version = backupFile.getVersion(backupId);
        if (version == null) {
            throw new IllegalArgumentException(String.format("File version for backup ID %d not found", backupId));
        }
        openArchiveFile();
        archiveFile.seek(version.getOffset());
        long length = version.getLength();
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(path));
            byte[] buffer = new byte[BUFFER_SIZE];
            long read = 0L;
            long written = 0L;
            while ((read = archiveFile.read(buffer)) > 0 && written < length) {
                if ((written + read) > length) {
                    // Do not read past end of file. 
                    read = length - written;
                }
                os.write(buffer, 0, (int) read);
            }
            System.out.println("Restored " + path);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    System.err.println("Could not close output stream");
                }
            }
            closeArchiveFile();
        }
    }
    
    private void openArchiveFile() throws IOException {
        String archivePath = String.format("%s/%s.dbx", destinationFolder, name);
        archiveFile = new RandomAccessFile(archivePath, "rw");
    }
    
    private void closeArchiveFile() {
        if (archiveFile != null) {
            try {
                archiveFile.close();
            } catch (IOException e) {
                System.err.println("Could not close archive file: " + e.getMessage());
            }
        }
    }
    
    private void backupFolder(File dir, int backupId) throws IOException {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                backupFolder(file, backupId);
            } else {
                backupFile(file, backupId);
            }
        }
    }
    
    private void backupFile(File file, int backupId) throws IOException {
        String path = file.getAbsolutePath();
        long date = file.lastModified();
        long length = file.length();
        BackupFile backupFile = files.get(path);
        if (backupFile == null) {
            // New file; create first version.
            long offset = backupFileVersion(file);
            backupFile = new BackupFile(path);
            backupFile.addVersion(backupId, date, offset, length);
            files.put(path, backupFile);
            System.out.println("A " + path);
        } else {
            // Existing file.
            BackupFileVersion version = backupFile.getCurrentVersion();
            if (version.getDate() == date) {
                // Unchanged file; do nothing.
            } else {
                // Updated file; add new version.
                long offset = backupFileVersion(file);
                backupFile.addVersion(backupId, date, offset, length);
                System.out.println("U " + path);
            }
        }
    }
    
    private long backupFileVersion(File file) throws IOException {
        long offset = archiveFile.length();
        archiveFile.seek(offset);
        InputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[BUFFER_SIZE];
            int length = 0;
            while ((length = bis.read(buffer)) > 0) {
                archiveFile.write(buffer, 0, length);
            }
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    System.err.println("Could not close input stream");
                }
            }
        }
        return offset;
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
                        long offset = dis.readLong();
                        long length = dis.readLong();
                        backupFile.addVersion(backupId, date, offset, length);
                    }
                    files.put(path, backupFile);
                }
            } catch (IOException e) {
                System.err.println("Could not read backup index file: " + e.getMessage());
            } finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e) {
                        System.err.println("Could not close input stream");
                    }
                }
            }
        }
    }
    
    private void writeIndexFile() {
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
                    dos.writeLong(version.getOffset());
                    dos.writeLong(version.getLength());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not write backup index file: " + e.getMessage());
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    System.err.println("Could not close output stream");
                }
            }
        }
    }
    
}
