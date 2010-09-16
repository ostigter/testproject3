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

/**
 * Version of a backup'ed file.
 * 
 * @author Oscar Stigter
 */
public class BackupFileVersion {
    
    /** Backup ID. */
    private final int backupId;
    
    /** The file date. */
    private final long date;
    
    /** Byte offset inside the project's archive file. */
    private long offset;
    
    /** The file length in bytes. */
    private long length;
    
    /**
     * Constructor.
     * 
     * @param backupId
     *            The backup ID.
     * @param date
     *            The file date.
     * @param offset
     *            The byte offset inside the project's archive.
     * @param length
     *            The file length.
     */
    public BackupFileVersion(int backupId, long date, long offset, long length) {
        this.backupId = backupId;
        this.date = date;
        this.offset = offset;
        this.length = length;
    }
    
    /**
     * Returns the backup ID.
     * 
     * @return The backup ID.
     */
    public int getBackupId() {
        return backupId;
    }
    
    /**
     * Returns the file date.
     * 
     * @return The file date.
     */
    public long getDate() {
        return date;
    }
    
    /**
     * Returns the byte offset of the file inside the archive file.
     * 
     * @return The offset.
     */
    public long getOffset() {
        return offset;
    }
    
    /**
     * Returns the file length.
     * 
     * @return The file length.
     */
    public long getLength() {
        return length;
    }
    
}
