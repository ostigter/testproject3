package org.ozsoft.backup.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Generic utility class for working with files and directories.
 * 
 * @author Oscar Stigter
 */
public abstract class FileHelper {
    
    /**
     * Creates a text file with a specific content. <br />
     * <br />
     * 
     * A previously existing file will be overwritten.
     * 
     * @param dir
     *            The directory to create the file in.
     * @param name
     *            The filename.
     * @param content
     *            The file content.
     * 
     * @return The created text file.
     * 
     * @throws IOException
     *             If the file could not be written.
     */
    public static File writeTextFile(File dir, String name, String content) throws IOException {
        File file = new File(dir, name);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
        } catch (IOException e) {
            throw e;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    // Best effort; ignore.
                }
            }
        }
        return file;
    }
    
    /**
     * Deletes a file or directory.
     * 
     * @param path
     *            The path to the file.
     */
    public static void deleteFile(String path) {
        deleteFile(new File(path));
    }
    
    /**
     * Deletes a file or directory.
     * 
     * @param file
     *            The file or directory.
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    deleteFile(child);
                }
            }
            file.delete();
        }
    }

    /**
     * Cleans a directory, deleting all of its contents. <br />
     * <br />
     * 
     * The directory itself will not be deleted.
     * 
     * @param dir
     *            The directory.
     */
    public static void cleanDir(File dir) {
        if (!dir.isDirectory()) {
            throw new IllegalStateException("Directory does not exist: " + dir);
        }
        for (File file : dir.listFiles()) {
            deleteFile(file);
        }
    }
    
}
