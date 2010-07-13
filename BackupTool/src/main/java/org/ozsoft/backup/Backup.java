package org.ozsoft.backup;

import java.util.Date;

public class Backup {
    
    private final int id;
    
    private final Date date;
    
    public Backup(int id) {
        this.id = id;
        date = new Date();
    }
    
    public int getId() {
        return id;
    }
    
    public Date getDate() {
        return date;
    }

}
