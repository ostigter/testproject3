package org.ozsoft.backup;

public class Backup {
    
    private final int id;
    
    private final long date;
    
    public Backup(int id, long date) {
        this.id = id;
        this.date = date;
    }
    
    public int getId() {
        return id;
    }
    
    public long getDate() {
        return date;
    }

}
