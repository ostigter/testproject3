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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * A backup'ed file (versioned).
 * 
 * @author Oscar Stigter
 */
public class BackupFile {
    
    /** Full file path. */
    private final String path;
    
    /** The versions mapped by backup date. */
    private final Map<Long, BackupFileVersion> versions;
    
    /** Backup date of the latest version. */
    private long latestVersion = 0;
    
    /**
     * Constructor.
     * 
     * @param path
     *            The full file path.
     */
    public BackupFile(String path) {
        this.path = path;
        versions = new TreeMap<Long, BackupFileVersion>();
    }
    
    /**
     * Returns the full file path.
     * 
     * @return The full file path.
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Returns all file versions.
     * 
     * @return All file versions.
     */
    public Collection<BackupFileVersion> getVersions() {
        return Collections.unmodifiableCollection(versions.values());
    }
    

    /**
     * Adds a file version.
     * 
     * @param backupDate
     *            The backup date.
     * @param date
     *            The file date.
     * @param offset
     *            The offset in the archive file.
     * @param length
     *            The file length.
     */
    public void addVersion(long backupDate, long fileDate, long offset, long length) {
        versions.put(backupDate, new BackupFileVersion(backupDate, fileDate, offset, length));
        latestVersion = backupDate;
    }

    /**
     * Returns the file version belonging to specific backup.
     * 
     * @param backupDate
     *            The backup date.
     * 
     * @return The file version if found, otherwise null.
     */
    public BackupFileVersion getVersion(long backupDate) {
        return versions.get(backupDate);
    }
    
    /**
     * Returns the latest file version.
     * 
     * @return The latest file version.
     */
    public BackupFileVersion getLatestVersion() {
        return versions.get(latestVersion);
    }

    /**
     * Returns the previous version in relation to a specific backup.
     * 
     * @param backupDate
     *            The backup date.
     * 
     * @return The previous version, or null if not found.
     */
    public BackupFileVersion getPreviousVersion(long backupDate) {
        BackupFileVersion previousVersion = null;
        for (BackupFileVersion version : versions.values()) {
            if (version.getBackupDate() != backupDate) {
                previousVersion = version;
            } else {
                // No more previous versions.
                break;
            }
        }
        return previousVersion;
    }
    
    /**
     * Removes the file version belonging to a specific backup.
     * 
     * @param backupDate
     *            The backup date.
     */
    public void removeVersion(long backupDate) {
        versions.remove(backupDate);
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return path;
    }
    
}
