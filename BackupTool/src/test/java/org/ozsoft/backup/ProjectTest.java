package org.ozsoft.backup;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ozsoft.backup.util.FileHelper;

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
        File dir = new File(TEST_DIR);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Cleans up the environment after all tests have run.
     */
    @AfterClass
    public static void afterClass() {
        // Delete temporary test directory. 
        FileHelper.deleteFile(TEST_DIR);
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
     * Tests creating a backup.
     * 
     * @throws IOException
     *             In case of an I/O error.
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

        // Create initial backup (no files).
        project.createBackup();

        // Create some files.
        FileHelper.writeTextFile(folder1, "file-1001.txt", "1001_v1");
        FileHelper.writeTextFile(folder1, "file-1002.txt", "1002_v1");
        FileHelper.writeTextFile(folder2, "file-2001.txt", "2001_v1");
        FileHelper.writeTextFile(folder2, "file-2002.txt", "2002_v1");
        
        // Create another backup (4 new files).
        project.createBackup();

        // Update a file.
        FileHelper.writeTextFile(folder1, "file-1001.txt", "1001_v2");
        // Delete a file.
        FileHelper.deleteFile(new File(folder2, "file-2002.txt"));

        // Create another backup (1 updated file).
        project.createBackup();
    }
    
}
