package org.ozsoft.backup;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ozsoft.backup.util.FileHelper;

/**
 * Test suite for the BackupTool.
 * 
 * @author Oscar Stigter
 */
public class ProjectTest {
    
    /** Temporary test directory. */
    private static final String TEST_DIR = "target/test"; 
    
    @BeforeClass
    public static void beforeClass() {
        // Create temporary test directory.
        File dir = new File(TEST_DIR);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
    }
    
    @AfterClass
    public static void afterClass() {
        FileHelper.deleteFile(TEST_DIR);
    }
    
    @Before
    public void before() {
        // Clean test directories.
        FileHelper.cleanDir(new File(TEST_DIR));
    }
    
    @Test
    public void backup() throws IOException {
        Project project = new Project("Project1");
        project.addSourceFolder(TEST_DIR + "/folder1");
        project.addSourceFolder(TEST_DIR + "/folder2");
        project.addDestinationFolder(TEST_DIR + "/backup");
        
        File dir = new File(TEST_DIR, "folder1");
        dir.mkdir();
        FileHelper.createTextFile(dir, "file-1001.txt", "1001_v1");
        FileHelper.createTextFile(dir, "file-1002.txt", "1002_v1");
        dir = new File(TEST_DIR, "folder2");
        dir.mkdir();
        FileHelper.createTextFile(dir, "file-2001.txt", "2001_v1");
        FileHelper.createTextFile(dir, "file-2002.txt", "2002_v1");
        
        project.createBackup();
    }
    
}
