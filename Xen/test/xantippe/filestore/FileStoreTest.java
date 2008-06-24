package xantippe.filestore;


import java.io.File;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import xantippe.Util;
import xantippe.filestore.FileStore;
import xantippe.filestore.FileStoreException;


public class FileStoreTest {
	

	private static final String DB_DIR = "test_db";
	
	private static final String DOCS_DIR = "test_docs";
	
	
	@BeforeClass
	public static void beforeClass() {
		Util.initLog4j();
		deleteFile(DB_DIR);
	}
	
	
	@AfterClass
	public static void afterClass() {
		deleteFile(DB_DIR);
	}
	
	
	@Test
	public void test() {
		FileStore store = new FileStore(DB_DIR);

		try {
			
			store.start();
			
			Assert.assertEquals(0, store.size());
			
			// Add file.
			store.store(1, new File(DOCS_DIR + "/0001.xml"));
			Assert.assertEquals(1, store.size());
			
			// Add file.
			store.store(2, new File(DOCS_DIR + "/0002.xml"));
			Assert.assertEquals(2, store.size());
			
			// Update file.
			store.store(2, new File(DOCS_DIR + "/0002.xml"));
			Assert.assertEquals(2, store.size());
			
			// Add file.
			store.store(3, new File(DOCS_DIR + "/0003.xml"));
			Assert.assertEquals(3, store.size());
			
			// Delete file.
			store.delete(2);
			Assert.assertEquals(2, store.size());
			
			// Delete all files.
			store.deleteAll();
			Assert.assertEquals(0, store.size());
			
			store.shutdown();
			
		} catch (FileStoreException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	
	private static void deleteFile(String path) {
		deleteFile(new File(path));
	}
	

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
