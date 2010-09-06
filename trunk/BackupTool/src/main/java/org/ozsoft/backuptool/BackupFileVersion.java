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
    
    /** Backup date. */
    private final long backupDate;
    
    /** The file date at time of the backup. */
    private final long fileDate;
    
    /** Byte offset inside the project's archive file. */
    private long offset;
    
    /** The file length in bytes. */
    private long length;
    
    /**
     * Constructor.
     * 
     * @param backupDate
     *            The backup date.
     * @param fileDate
     *            The file date.
     * @param offset
     *            The byte offset inside the project's archive.
     * @param length
     *            The file length.
     */
    public BackupFileVersion(long backupDate, long fileDate, long offset, long length) {
        this.backupDate = backupDate;
        this.fileDate = fileDate;
        this.offset = offset;
        this.length = length;
    }
    
    /**
     * Returns the backup date.
     * 
     * @return The backup date.
     */
    public long getBackupDate() {
        return backupDate;
    }
    
    /**
     * Returns the file date at time of the backup.
     * 
     * @return The file date.
     */
    public long getFileDate() {
        return fileDate;
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
