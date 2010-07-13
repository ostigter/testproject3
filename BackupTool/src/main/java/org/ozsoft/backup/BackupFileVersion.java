package org.ozsoft.backup;

public class BackupFileVersion {
    
    private final long date;
    
    public BackupFileVersion(long date) {
        this.date = date;
    }
    
    public long getDate() {
        return date;
    }
    
}
