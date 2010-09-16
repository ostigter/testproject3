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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ozsoft.backup.util.FileHelper;
import org.ozsoft.backuptool.Backup;
import org.ozsoft.backuptool.BackupFile;
import org.ozsoft.backuptool.BackupFileVersion;
import org.ozsoft.backuptool.Project;

/**
 * Test suite for the Project class.
 * 
 * @author Oscar Stigter
 */
public class ProjectTest {
    
    /** Temporary test directory. */
    private static final String TEST_DIR = "target/test"; 
    
    /**
     * Sets up the environment before any test is run.
     */
    @BeforeClass
    public static void beforeClass() {
        // Create temporary test directory.
        new File(TEST_DIR).mkdir();
    }
    
    /**
     * Cleans up the environment after all tests have run.
     */
    @AfterClass
    public static void afterClass() {
        // Delete temporary test directory. 
        FileHelper.deleteFile(new File(TEST_DIR));
    }
    
    /**
     * Sets up the environment before each individual test.
     */
    @Before
    public void before() {
        // Clean temporary test directory.
        FileHelper.cleanDir(new File(TEST_DIR));
    }
    
    /**
     * Tests creating and restoring backups.
     */
    @Test
    public void backup() throws IOException {
        Project project = new Project("Project1");
        project.addSourceFolder(TEST_DIR + "/folder1");
        project.addSourceFolder(TEST_DIR + "/folder2");
        project.setDestinationFolder(TEST_DIR + "/backup");
        
        // Create some (empty) source folders.
        File folder1 = new File(TEST_DIR, "folder1");
        folder1.mkdir();
        File folder2 = new File(TEST_DIR, "folder2");
        folder2.mkdir();

        // Create backup with ID 1 (no files).
        project.createBackup();
        
        // Test backup.
        Backup[] backups = project.getBackups();
        Assert.assertEquals(1, backups.length);
        Map<String, BackupFile> backupFiles = project.getBackupFiles();
        Assert.assertEquals(0, backupFiles.size());

        // Create some files.
        FileHelper.writeTextFile(new File(folder1, "file-1001.txt"), "1001_v1");
        FileHelper.writeTextFile(new File(folder1, "file-1002.txt"), "1002_v1");
        FileHelper.writeTextFile(new File(folder2, "file-2001.txt"), "2001_v1");
        FileHelper.writeTextFile(new File(folder2, "file-2002.txt"), "2002_v1");
        
        // Create backup with ID 2 (4 new files).
        project.createBackup();

        // Update a file.
        FileHelper.writeTextFile(new File(folder1, "file-1001.txt"), "1001_v2");

        // Delete a file.
        FileHelper.deleteFile(new File(folder2, "file-2001.txt"));

        // Create backup with ID 3 (1 updated file, 1 deleted file).
        project.createBackup();

        // Create backup with ID 4 (no changes).
        project.createBackup();
        
        // Test backups.
        backups = project.getBackups();
        Assert.assertEquals(4, backups.length);
        backupFiles = project.getBackupFiles();
        Assert.assertEquals(4, backupFiles.size());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BackupFile file : backupFiles.values()) {
            System.out.println(file);
            for (BackupFileVersion version : file.getVersions()) {
                System.out.format("  %s, %s, %d, %d\n",
                        version.getBackupId(), dateFormat.format(version.getDate()), version.getOffset(), version.getLength());
            }
        }
        
        // Delete all files.
        FileHelper.deleteFile(folder1);
        FileHelper.deleteFile(folder2);

        // Restore files.
        File file = new File(folder1, "file-1001.txt");
        project.restoreFile(file.getAbsolutePath(), -1);
        Assert.assertTrue("File not restored", file.exists());
        Assert.assertTrue(FileHelper.readTextFile(file).contains("1001_v2"));

        FileHelper.deleteFile(file);
        project.restoreFile(file.getAbsolutePath(), 2);
        Assert.assertTrue("File not restored", file.exists());
        Assert.assertTrue(FileHelper.readTextFile(file).contains("1001_v1"));

        FileHelper.deleteFile(file);
        project.restoreFile(file.getAbsolutePath(), 3);
        Assert.assertTrue("File not restored", file.exists());
        Assert.assertTrue(FileHelper.readTextFile(file).contains("1001_v2"));

        file = new File(folder1, "file-1002.txt");
        project.restoreFile(file.getAbsolutePath(), -1);
        Assert.assertTrue("File not restored", file.exists());
        Assert.assertTrue(FileHelper.readTextFile(file).contains("1002_v1"));
        
        // Delete backups.
        project.deleteBackup(-1);  // Unknown backup date.
        backups = project.getBackups();
        Assert.assertEquals(4, backups.length);

        project.deleteBackup(1);
        backups = project.getBackups();
        Assert.assertEquals(3, backups.length);
        
        project.deleteBackup(2);
        backups = project.getBackups();
        Assert.assertEquals(2, backups.length);
        
        project.deleteBackup(3);
        backups = project.getBackups();
        Assert.assertEquals(1, backups.length);
        
        project.deleteBackup(4);
        backups = project.getBackups();
        Assert.assertEquals(0, backups.length);
    }
    
}
