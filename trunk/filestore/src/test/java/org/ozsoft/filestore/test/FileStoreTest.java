package org.ozsoft.filestore.test;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ozsoft.filestore.FileStore;
import org.ozsoft.filestore.FileStoreException;

/**
 * Test suite for the FileStore class.
 * 
 * @author Oscar Stigter
 */
public class FileStoreTest {
    
    /** Directory with the data file. */
    private static final String DATA_DIR = "target/test-data";
    
    /** Directory with the test resource files. */
    private static final File RESOURCES_DIR = new File("src/test/resources/data");
    
    /**
     * Sets up the test environment.
     */
    @Before
    public void before() {
        // Clean any previous data directory.
        deleteFile(new File(DATA_DIR));
    }
    
    /**
     * Tests the file store.
     */
    @Test
    public void main() {
        FileStore fileStore = new FileStore();
        fileStore.setDataDir(DATA_DIR);
        try {
            fileStore.start();
            fileStore.store(1, new File(RESOURCES_DIR, "foo.txt"));
            fileStore.store(2, new File(RESOURCES_DIR, "bar.txt"));
            fileStore.shutdown();
        } catch (FileStoreException e) {
            Assert.fail(e.getMessage());
        }
    }
    
    /**
     * Deletes a file or directory (recursively).
     * 
     * @param  file  the file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deleteFile(f);
                }
            }
            file.delete();
        }
    }
    
}
