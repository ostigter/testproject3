package org.ozsoft.filestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

/**
 * File system based database for storing files.
 * 
 * Files are logically stored, retrieved and deleted based on ID.
 * 
 * The file contents are stored in a binary file (by default "data.dbx") in a
 * configurable directory (by default "data"). If this directory does not
 * exists, it will be created.
 * 
 * When storing a file, a previous file with the same ID will be overwritten.
 * 
 * The positioning algorithm for storing a file is simple; it inserts the file
 * at the first free position it fits in, otherwise it appends it at the end.
 * 
 * For performance reasons, this class offers no synchronization or locking, and
 * is thus NOT thread-safe.
 * 
 * @author Oscar Stigter
 */
public class FileStore {
    
    /** Default database directory. */
    private static final String DEFAULT_DATA_DIR = "data";
    
    /** File with the file contents. */
    private static final String DATA_FILE = "data.dbx";
    
    /** File header length (offset + length). */
    private static final int HEADER_LENGTH = 8;
    
    /** Buffer size. */
    private static final int BUFFER_SIZE = 8192; // 8 KB
    
    /** Log */
    private static final Logger LOG = Logger.getLogger(FileStore.class);
    
    /** Document entries mapped by the their ID. */
    private final Map<Integer, FileEntry> entries;
    
    /** Data directory. */
    private File dataDir = new File(DEFAULT_DATA_DIR);
    
    /** Data file with the file contents. */ 
    private RandomAccessFile dataFile;
    
    /** Indicates whether the FileStore is running. */
    private boolean isRunning = false;
    
    /**
     * Constructor.
     */
    public FileStore() {
        entries = new TreeMap<Integer, FileEntry>();
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

        LOG.debug("Starting");
            
        // Create data directory.
        if (!dataDir.exists()) {
            if (!dataDir.mkdirs()) {
                String msg = "Could not create data directory: " + dataDir;
                LOG.error(msg);
                throw new FileStoreException(msg);
            }
        }
        
        try {
            // Open data file.
            dataFile = new RandomAccessFile(new File(dataDir, DATA_FILE), "rw");
        } catch (IOException e) {
            String msg = String.format("Error opening data file '%s'", DATA_FILE);
            throw new FileStoreException(msg, e);
        }

        try {
            // Read index file.
            readIndices();
        } catch (IOException e) {
            String msg = String.format("Error reading file '%s'", DATA_FILE);
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
        
        LOG.debug("Shutting down");
        
        isRunning = false;
        try {
            dataFile.close();
        } catch (IOException e) {
            String msg = "Error closing data file";
            LOG.error(msg, e);
            throw new FileStoreException(msg, e);
        } finally {
            dataFile = null;
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
    public boolean exists(int id) {
        return entries.containsKey(id);
    }
    
    /**
     * Stores a file based on a file.
     * 
     * @param   id    the file ID
     * @param   file  the file with the file's content
     * 
     * @throws  FileStoreException
     *              if the file could not be read, or the file content
     *              could not be written
     */
    public void store(int id, File file) throws FileStoreException {
        // Delete (overwrite) any previous file.
        delete(id);
        
        int length = (int) file.length();
        int offset = findFreePosition(length);
        
        FileEntry entry = new FileEntry(id);
        entry.setOffset(offset);
        entry.setLength(length);
        entries.put(id, entry);

        try {
            dataFile.seek(offset);
            dataFile.writeInt(id);
            dataFile.writeInt(length);
            InputStream is = new FileInputStream(file);
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) > 0) {
                dataFile.write(buffer, 0, bytesRead);
            }
            is.close();
        } catch (IOException e) {
            entries.remove(id);
            String msg = String.format("Could not store file with ID %d", id);
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
     * @throws  FileStoreException
     *              if the file could not be found, or the content could
     *              not be read
     */
    public InputStream retrieve(int id) throws FileStoreException {
        InputStream is = null;
        FileEntry entry = entries.get(id);
        if (entry != null) {
            try {
                is = new RetrieveStream(dataFile, entry.getOffset() + HEADER_LENGTH, entry.getLength());
            } catch (IOException e) {
                String msg = String.format("Error retrieving file with ID %d", id);
                LOG.error(msg, e);
                throw new FileStoreException(msg, e);
            }
        } else {
            throw new FileStoreException(String.format("File with ID %d not found", id));
        }
        return is;
    }
    
    /**
     * Returns the length of a file.
     * 
     * When the specified file does not exist, 0 is returned.
     *  
     * @param  id  the file ID
     * 
     * @return  the file length in bytes
     */
    public int getLength(int id) {
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
     */
    public void delete(int id) throws FileStoreException {
        FileEntry entry = entries.get(id);
        if (entry != null) {
            try {
                // Mark file as deleted.
                dataFile.seek(entry.getOffset());
                dataFile.writeInt(-1);
                // Remove file entry.
                entries.remove(id);
            } catch (IOException e) {
                LOG.error(String.format("Error marking file %d as deleted", id), e);
            }
        }
    }
    
    /**
     * Logs a message showing the disk size usage.
     */
    public void printSizeInfo() {
        long stored = getStoredSpace();
        long used = getUsedSpace();
        long wasted = stored - used;
        double wastedPerc = 0.0;
        if (stored > 0) {
            wastedPerc = ((double) wasted / (double) stored) * 100;
        }
        LOG.debug(String.format(Locale.US,
                "Disk usage:  Size: %s, Used: %s, Wasted: %s (%.1f %%)",
                diskSizeToString(stored), diskSizeToString(used),
                diskSizeToString(wasted), wastedPerc));
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
     * fits a file with the specified length.
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
            if (free >= (HEADER_LENGTH + length)) {
                // Found a suitable spot!
                break;
            } else {
                // Proceed to next entry.
                offset = entry.getOffset() + HEADER_LENGTH + entry.getLength();
            }
        }
        return offset;
    }
    
    /**
     * Reads the file indices.
     * 
     * @throws IOException
     *             if the data file could not be read.
     */
    private void readIndices() throws IOException {
        entries.clear();
        long fileLength = dataFile.length();
        int offset = 0;
        dataFile.seek(offset);
        while (offset < fileLength) {
            int id = dataFile.readInt();
            int length = dataFile.readInt();
            if (id != -1) {
                FileEntry entry = new FileEntry(id);
                entry.setOffset(offset);
                entry.setLength(length);
                entries.put(id, entry);
            }
            offset += HEADER_LENGTH + length;
            dataFile.skipBytes(length);
        }
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
            String msg = "Error retrieving data file length";
            LOG.error(msg, e);
        }
        return size;
    }
    
    /**
     * Returns the net used disk space for storing the files without any
     * fragmentation.
     * 
     * @return  The net used disk space 
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
        
        private final int id;
        
        private int offset;
        
        private int length;

        public FileEntry(int id) {
            this.id = id;
        }

        public int getId() {
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

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override // Object
        public int hashCode() {
            return id;
        }
        
        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof FileEntry) {
                FileEntry entry = (FileEntry) obj;
                return entry.getId() == id;
            } else {
                return false;
            }
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        @Override
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

        /*
         * (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return String.format("{%d, %d, %d}", id, offset, length);
        }

    } // FileEntry

}
