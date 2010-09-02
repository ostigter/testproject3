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

package org.ozsoft.backup;

/**
 * Version of a backup'ed file.
 * 
 * @author Oscar Stigter
 */
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
