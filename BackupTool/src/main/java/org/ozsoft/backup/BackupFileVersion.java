package org.ozsoft.backup;

public class BackupFileVersion {
    
    private final int backupId;
    
    private final long date;
    
    private long offset;
    
    private long length;
    
    public BackupFileVersion(int backupId, long date, long offset, long length) {
        this.backupId = backupId;
        this.date = date;
        this.offset = offset;
        this.length = length;
    }
    
    public int getBackupId() {
        return backupId;
    }
    
    public long getDate() {
        return date;
    }
    
    public long getOffset() {
        return offset;
    }
    
    public long getLength() {
        return length;
    }
    
}
