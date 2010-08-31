package org.ozsoft.backup.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Generic utility class for working with files and directories.
 * 
 * @author Oscar Stigter
 */
public abstract class FileHelper {
    
    /** Platform specific NEWLINE character. */
    private static final String NEWLINE = System.getProperty("line.separator");
    
    /**
     * Reads the content of a text file.
     * 
     * @param file
     *            The file.
     * @return The content.
     * 
     * @throws IOException
     *             If the file could not be read.
     */
    public static String readTextFile(File file) throws IOException {
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(NEWLINE);
            }
            content = sb.toString();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // Best effort; ignore.
                }
            }
        }
        return content;
    }
    
    /**
     * Writes a text file with the specified content. <br />
     * <br />
     * 
     * A previously existing file will be overwritten.
     * 
     * @param file
     *            The file.
     * @param content
     *            The content.
     * 
     * @throws IOException
     *             If the file could not be written.
     */
    public static void writeTextFile(File file, String content) throws IOException {
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
            boolean deleted = file.delete();
            if (!deleted) {
                throw new IllegalStateException("Could not delete file or directory: " + file);
            }
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
