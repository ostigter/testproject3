package org.ozsoft.filestore.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

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
    public void test() {
        FileStore fileStore = new FileStore();
        fileStore.setDataDir(DATA_DIR);
        try {
            fileStore.start();
            
            Assert.assertFalse(fileStore.exists(1));
            
            fileStore.store(1, new File(RESOURCES_DIR, "foo.txt"));
            Assert.assertTrue(fileStore.exists(1));
            Assert.assertEquals("Foo", retrieve(fileStore, 1));
            
            fileStore.store(2, new File(RESOURCES_DIR, "bar.txt"));
            Assert.assertTrue(fileStore.exists(2));
            Assert.assertEquals("Bar", retrieve(fileStore, 2));
            
            fileStore.store(1, new File(RESOURCES_DIR, "cafe.txt"));
            Assert.assertTrue(fileStore.exists(1));
            Assert.assertEquals("Cafe", retrieve(fileStore, 1));
            
            fileStore.store(1, new File(RESOURCES_DIR, "foo.txt"));
            Assert.assertTrue(fileStore.exists(1));
            Assert.assertEquals("Foo", retrieve(fileStore, 1));

            fileStore.store(3, new File(RESOURCES_DIR, "cafe.txt"));
            Assert.assertTrue(fileStore.exists(3));
            Assert.assertEquals("Cafe", retrieve(fileStore, 3));
            
            fileStore.delete(3);
            Assert.assertFalse(fileStore.exists(3));

            fileStore.shutdown();
            
            fileStore.start();
            Assert.assertTrue(fileStore.exists(1));
            Assert.assertTrue(fileStore.exists(2));
            Assert.assertFalse(fileStore.exists(3));
            fileStore.shutdown();
        } catch (FileStoreException e) {
            Assert.fail(e.getMessage());
        }
    }
    
    /**
     * Retrieves the content of a file as a String value.
     * 
     * @param fileStore
     *            The file store.
     * @param id
     *            The file ID.
     * 
     * @return The file content.
     * 
     * @throws FileStoreException
     *             If the file content could not be retrieved.
     */
    private static String retrieve(FileStore fileStore, int id) throws FileStoreException {
        String content = null;
        try {
            Reader reader = new InputStreamReader(fileStore.retrieve(id));
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[8192];
            int read = 0;
            while ((read = reader.read(buffer)) > 0) {
                sb.append(buffer, 0, read);
            }
            reader.close();
            content = sb.toString();
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
        return content;
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
