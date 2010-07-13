package org.ozsoft.backup;

public class BackupFileVersion {
    
    private final int backupId;
    
    private final long date;
    
    public BackupFileVersion(int backupId, long date) {
        this.backupId = backupId;
        this.date = date;
    }
    
    public int getBackupId() {
        return backupId;
    }
    
    public long getDate() {
        return date;
    }
    
}
