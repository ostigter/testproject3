package org.ozsoft.backup;

import java.util.Date;

public class Backup {
    
    private final Date date;
    
    public Backup() {
        date = new Date();
    }
    
    public Date getDate() {
        return date;
    }

}
