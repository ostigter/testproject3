// This file is part of the BackupTool project.
//
// Copyright 2010 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.backuptool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Backup.
 * 
 * @author nlost
 */
public class Backup {
    
    /** Date format. */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    /** Backup ID. */
    private final int id;
    
    /** Backup date. */
    private final long date;
    
    /**
     * Constructor.
     * 
     * @param id
     *            The backup ID.
     * @param date
     *            The backup date.
     */
    public Backup(int id, long date) {
        this.id = id;
        this.date = date;
    }
    
    /**
     * Returns the backup ID.
     * 
     * @return The backup ID.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Returns the backup date.
     * 
     * @return The backup date.
     */
    public long getDate() {
        return date;
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("%d: %s", id, DATE_FORMAT.format(date));
    }

}
