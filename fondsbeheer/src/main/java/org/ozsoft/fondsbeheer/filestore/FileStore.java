package org.ozsoft.fondsbeheer.filestore;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * File system based database for storing files.
 *
 * Files are logically stored, retrieved, listed and deleted based on a name.
 * The file contents are handled as a byte array.
 *
 * Files are physically stored in a single binary file ('data.dbx').
 * File positions are stored in a second binary file ('index.dbx').
 *
 * All database files are stored in a configurable directory (by default
 * 'data'). If this directory does not exists, it will be created.
 *
 * The positioning algorithm for storing a file is simple; it just inserts the
 * file at the first free position it fits in, or it appends the file at the
 * end.
 *
 * @author Oscar Stigter
 */
public class FileStore {

    /** Default database directory. */
    private static final String DEFAULT_DATA_DIR = "data";
    
    /** Index file with the file entries (FAT). */
    private static final String INDEX_FILE = "index.dbx";
    
    /** Data file with the file contents. */
    private static final String DATA_FILE = "data.dbx";
    
    /** log4j logger. */
    private static final Logger LOG = Logger.getLogger(FileStore.class);
    
    /** Document entries mapped by the their ID. */
    private final Map<String, FileEntry> entries;
    
    /** Indicates whether the FileStore is running. */
    private boolean isRunning = false;
    
    /** Data directory. */
    private File dataDir = new File(DEFAULT_DATA_DIR);
    
    /** Data file with the file contents. */ 
    private RandomAccessFile dataFile;
    
    /**
     * Constructor.
     */
    public FileStore() {
        entries = new TreeMap<String, FileEntry>();
    }
    
    /**
     * Returns the data directory.
     * 
     * @return  the data directory
     */
    public String getDataDir() {
        return dataDir.getAbsolutePath();
    }

    /**
     * Sets the data directory.
     * 
     * The data directory may only be set if the database is not running.
     * 
     * @param path  the data directory
     * 
     * @throws IllegalArgumentException
     *             if the path is null or empty
     * @throws IllegalStateException
     *             if the FileStore is running
     */
    public void setDataDir(String path) {
        if (path == null || path.length() == 0) {
            throw new IllegalArgumentException("Null or empty path");
        }

        if (isRunning) {
            throw new IllegalStateException("FileStore is running");
        }

        dataDir = new File(path);
    }

    /**
     * Starts the FileStore.
     *
     * @throws  IllegalStateException
     *              if the database is already running
     * @throws  FileStoreException
     *             if the data directory could not be created, the index file
     *             could not be read, or the data file could not be opened
     */
    public void start() throws FileStoreException {
        if (isRunning) {
            throw new IllegalStateException("Database already running");
        }

        // Create data directory.
        if (!dataDir.exists()) {
            if (!dataDir.mkdirs()) {
                String msg = "Could not create data directory: " + dataDir;
                LOG.error(msg);
                throw new FileStoreException(msg);
            }
        }
        
        try {
            // Read index file.
            readIndexFile();
        } catch (IOException e) {
            String msg = String.format("Error reading file '%s': %s",
                    INDEX_FILE, e.getMessage());
            throw new FileStoreException(msg, e);
        }
        
        try {
            // Open data file.
            dataFile = new RandomAccessFile(
                    new File(dataDir, DATA_FILE), "rw");
        } catch (IOException e) {
            String msg = String.format("Error opening data file '%s': %s",
                    DATA_FILE, e.getMessage());
            throw new FileStoreException(msg, e);
        }

        isRunning = true;
        
        LOG.debug("Started");
    }
    
    /**
     * Shuts down the FileStore.
     * 
     * @throws  IllegalStateException
     *              if the database is not running
     * @throws  FileStoreException
     *              if the index file could not be written, or data file could
     *              not be closed  
     */
    public void shutdown() throws FileStoreException {
        checkIsRunning();
        
        sync();
        
        try {
            dataFile.close();
            
        } catch (IOException e) {
            String msg = "Error closing data file: " + e.getMessage();
            LOG.error(msg, e);
            throw new FileStoreException(msg, e);
            
        } finally {
            isRunning = false;
        }
        
        entries.clear();
        
        LOG.debug("Shut down");
    }
    
    /**
     * Returns whether the FileStore is running.
     * 
     * @return  true if the FileStore is running, otherwise false
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Returns the number of stored files.
     * 
     * @return  the number of stored files
     * 
     * @throws  IllegalStateException
     *              if the database is not running
     */
    public int size() {
        return entries.size();
    }
    
    /**
     * Returns the ID's of the stored files.
     *
     * @return The ID's of the stored files.
     */
    public Set<String> getFileIds() {
        return entries.keySet();
    }
    
    /**
     * Indicates whether a stored file with the specified ID exists.
     *  
     * @param   id  the file ID
     * 
     * @return  true  if a stored file with the specified ID exists,
     *                otherwise false
     * 
     * @throws  IllegalStateException
     *              if the database is not running
     */
    public boolean contains(String id) {
        checkIsRunning();
        return entries.containsKey(id);
    }
    
    /**
     * Stores a file.
     * 
     * @param id
     *            The file ID.
     * @param content
     *            The file's content.
     * 
     * @throws IllegalStateException
     *             if the database is not running.
     * @throws FileStoreException
     *             if the file could not be stored.
     */
    public void store(String id, byte[] content) throws FileStoreException {
        checkIsRunning();
        
        if (LOG.isDebugEnabled()) {
        	LOG.debug(String.format("Storing file with ID '%s'", id));
        }
        
        // Delete (overwrite) any previous file entry with the same ID.
        entries.remove(id);
        
        // Find position to insert entry.
        int offset = findFreePosition(content.length);
        
        // Create a new file entry.
        FileEntry entry = new FileEntry(id);
        entry.setOffset(offset);
        entry.setLength(content.length);
        entries.put(id, entry);
        
        // Write the file content.
        try {
            dataFile.seek(offset);
            dataFile.write(content);
        } catch (IOException e) {
            entries.remove(id);
            String msg = String.format("Could not store file with ID '%s'", id);
            LOG.error(msg, e);
            throw new FileStoreException(msg, e);
        }
    }

    /**
     * Retrieves the content of a file.
     *  
     * @param   id  the file ID
     * 
     * @return  the file content
     * 
     * @throws  IllegalStateException
     *              if the database is not running
     * @throws  FileStoreException
     *              if the file could not be found, or the content could
     *              not be read
     */
    public byte[] retrieve(String id) throws FileStoreException {
        checkIsRunning();

        if (LOG.isDebugEnabled()) {
        	LOG.debug(String.format("Retrieving file with ID '%s'", id));
        }
        
        byte[] content = null;
        FileEntry entry = entries.get(id);
        if (entry != null) {
        	content = new byte[entry.getLength()];
            try {
            	dataFile.seek(entry.getOffset());
            	dataFile.read(content);
            } catch (IOException e) {
                String msg = String.format(
                		"Could not retrieve file with ID '%s'", id);
                LOG.error(msg, e);
                throw new FileStoreException(msg, e);
            }
        } else {
            throw new FileStoreException(
            		String.format("File with ID '%s' not found", id));
        }
        return content;
    }
    
    /**
     * Returns the length of a file.
     * 
     * When the specified file does not exist, 0 is returned.
     *  
     * @param  id  the file ID
     * 
     * @return  the file length in bytes
     * 
     * @throws  IllegalStateException
     *              if the database is not running
     */
    public int getLength(int id) {
        checkIsRunning();
        
        int length = 0;
        
        FileEntry entry = entries.get(id);
        if (entry != null) {
            length = entry.getLength();
        }
        
        return length;
    }
    
    /**
     * Deletes a file.
     * 
     * @param   id  the file ID
     * 
     * @throws  IllegalStateException
     *              if the database is not running
     */
    public void delete(int id) throws FileStoreException {
        checkIsRunning();
        
        FileEntry entry = entries.get(id);
        if (entry != null) {
            entries.remove(id);
            LOG.debug("Deleted file with ID " + id);
        }
    }
    
    /**
     * Writes any volatile meta-data to disk.
     */
    public void sync() throws FileStoreException {
        LOG.debug("Sync");
        if (isRunning) {
            try {
                writeIndexFile();
            } catch (IOException e) {
                String msg = "Error sync'ing to disk: " + e.getMessage();
                LOG.error(msg, e);
                throw new FileStoreException(msg, e);
            }
        }
    }

    /**
     * Logs various statistics.
     * 
     * @throws  IllegalStateException
     *              if the database is not running
     */
    public void logStatistics() {
        checkIsRunning();
        
        long stored = getStoredSpace();
        long used = getUsedSpace();
        long wasted = stored - used;
        double wastedPerc = 0.0;
        if (stored > 0) {
            wastedPerc = ((double) wasted / (double) stored) * 100;
        }
        LOG.info(String.format(Locale.US, "File count: %d, Allocated: %s, Used: %s, Wasted: %s (%.1f %%)",
                entries.size(), diskSizeToString(stored), diskSizeToString(used), diskSizeToString(wasted), wastedPerc));
    }

    /**
     * Checks that the database is running.
     * 
     * @throws  IllegalStateException  if the database is not running.
     */
    private void checkIsRunning() {
        if (!isRunning) {
            throw new IllegalStateException("FileStore not running");
        }
    }

    /**
     * Returns the offset of the first free position in the data file that
     * would fit a file with the specified length.
     * 
     * @param  length  the file length in bytes
     * 
     * @return  the offset  
     */
    private int findFreePosition(int length) {
        int offset = 0;
        for (FileEntry entry : entries.values()) {
            // Look for free space between entries.
            long free = entry.getOffset() - offset;
            if (free >= length) {
                // Found a suitable spot!
                break;
            } else {
                // Proceed to next entry.
                offset = entry.getOffset() + entry.getLength();
            }
        }
        return offset;
    }
    
    /**
     * Reads the index file.
     * 
     * @throws  IOException  if the file could not be read
     */
    private void readIndexFile() throws IOException {
        entries.clear();
        File file = new File(dataDir, INDEX_FILE);
        if (file.exists()) {
            DataInputStream dis =
                    new DataInputStream(new FileInputStream(file));
            int noOfEntries = dis.readInt();
            for (int i = 0; i < noOfEntries; i++) {
                String id = dis.readUTF();
                int offset = dis.readInt();
                int length = dis.readInt();
                FileEntry entry = new FileEntry(id);
                entry.setOffset(offset);
                entry.setLength(length);
                entries.put(id, entry);
            }
            dis.close();
        }
    }
    
    /**
     * Writes the index file.
     * 
     * @throws  IOException  if the file could not be written
     */
    private void writeIndexFile() throws IOException {
        File file = new File(dataDir, INDEX_FILE);
        DataOutputStream dos =
                new DataOutputStream(new FileOutputStream(file));
        dos.writeInt(entries.size());
        for (FileEntry entry : entries.values()) {
            dos.writeUTF(entry.getId());
            dos.writeInt(entry.getOffset());
            dos.writeInt(entry.getLength());
        }
        dos.close();
    }

    /**
     * Returns the size of the data file actually stored on disk.
     * 
     * @return  the size of the data file
     */
    private long getStoredSpace() {
        long size = 0L;
        try {
            size = dataFile.length();
        } catch (IOException e) {
            String msg =
                    "Error retrieving data file length: " + e.getMessage();
            LOG.error(msg, e);
        }
        return size;
    }
    
    /**
     * Returns the net used disk space for storing the files without any
     * fragmentation.
     * 
     * @return  the net used disk space 
     */
    private long getUsedSpace() {
        long size = 0L;
        for (FileEntry entry : entries.values()) {
            size += entry.getLength();
        }
        return size;
    }
    
    /**
     * Returns a human-friendly representation of a file size.
     *  
     * @param size  the file size in bytes
     * 
     * @return  the human-friendly representation of the file size
     */
    private static String diskSizeToString(long size) {
        String s = null;
        if (size >= 1073741824L) {
            s = String.format(Locale.US, "%.2f GB", size / 1073741824.0);
        } else if (size >= 1048576L) {
            s = String.format(Locale.US, "%.2f MB", size / 1048576.0);
        } else if (size >= 1024L) {
            s = String.format(Locale.US, "%.2f kB", size / 1024.0);
        } else {
            s = String.format(Locale.US, "%d bytes", size);
        }
        return s;
    }
 
    /**
     * Administrative entry of a stored file.
     * 
     * Each entry has a name, offset and length.
     * 
     * @author Oscar Stigter
     */
    private static class FileEntry implements Comparable<FileEntry> {
        
        private final String id;
        
        private int offset;
        
        private int length;

        public FileEntry(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        @Override
        public String toString() {
            return "{'" + id + "', " + offset + ", " + length + "}";
        }

        @Override // Object
        public int hashCode() {
            return id.hashCode();
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FileEntry) {
                return ((FileEntry) obj).id.equals(id);
            } else {
                return false;
            }
        }

        public int compareTo(FileEntry entry) {
            int otherOffset = entry.getOffset();
            if (offset > otherOffset) {
                return 1;
            } else if (offset < otherOffset) {
                return -1;
            } else {
                return 0;
            }
        }

    } // FileEntry

} // FileStore
