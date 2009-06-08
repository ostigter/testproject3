package org.ozsoft.fondsbeheer.test;

import java.io.File;

/**
 * Utility class.
 * 
 * @author Oscar Stigter
 */
public class Util {
	
	/**
	 * Private constructor to deny instantiation.
	 */
	private Util() {
		// Empty implementation.
	}

	/**
	 * Deletes a file or directory recursively.
	 * 
	 * @param path
	 *            The path to the file or directory.
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			deleteFile(file);
		}
	}

	/**
	 * Deletes a file or directory recursively.
	 * 
	 * @param file
	 *            The file or directory.
	 */
	public static void deleteFile(File file) {
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				deleteFile(f);
			}
		}
		file.delete();
	}

}
