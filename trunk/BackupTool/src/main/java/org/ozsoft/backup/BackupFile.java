package org.ozsoft.backup;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Backup'ed file (versioned).
 * 
 * @author Oscar Stigter
 */
public class BackupFile {
    
    private final String path;
    
    private final Map<Integer, BackupFileVersion> versions;
    
    private int currentVersion = 0;
    
    public BackupFile(String path) {
        this.path = path;
        versions = new TreeMap<Integer, BackupFileVersion>();
    }
    
    public String getPath() {
        return path;
    }
    
    public Collection<BackupFileVersion> getVersions() {
        return Collections.unmodifiableCollection(versions.values());
    }
    
    public void addVersion(int backupId, long date, long offset, long length) {
        versions.put(backupId, new BackupFileVersion(backupId, date, offset, length));
        currentVersion = backupId;
    }

    public BackupFileVersion getVersion(int backupId) {
        if (backupId > 0) {
            return versions.get(backupId);
        } else {
            return getCurrentVersion();
        }
    }
    
    public BackupFileVersion getCurrentVersion() {
        return versions.get(currentVersion);
    }

    public void removeVersion(int backupId) {
        versions.remove(backupId);
    }
    
    @Override
    public String toString() {
        return path;
    }
    
}
