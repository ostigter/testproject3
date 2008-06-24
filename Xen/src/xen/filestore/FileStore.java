package xen.filestore;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


/**
 * File system based database for storing files.
 *
 * Files are logically stored, retrieved, listed and deleted based on a name.
 * The file contents are handled as a byte array.
 *
 * Files are physically stored in a single binary file ('data.dbx').
 * File entries are stored in a second binary file ('index.dbx').
 *
 * All database files are stored in a configurable directory (by default
 * 'data'). If this directory does not exists, it will be created.
 *
 * The entryitioning algorithm for storing a file is simple; it just inserts the
 * file at the first free entryition it fits in, or it appends the file at the
 * end.
 *
 * @author Oscar Stigter
 */
public class FileStore {
    

	private static final String DEFAULT_DATA_DIR = "data";
	
    private static final String INDEX_FILE = "entries.dbx";
    
    private static final String DATA_FILE = "contents.dbx";
    
    private final Map<Integer, FileEntry> entries;
    
    private boolean isRunning = false;
    
    private String dataDirectory;
    
    private RandomAccessFile dataFile;
    

    //------------------------------------------------------------------------
    //  Constructors
    //------------------------------------------------------------------------
    

    public FileStore() {
        this(DEFAULT_DATA_DIR);
    }
    

    public FileStore(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        entries = new TreeMap<Integer, FileEntry>();
    }
    

    //------------------------------------------------------------------------
    //  Public methods
    //------------------------------------------------------------------------
    

    /**
     * Starts the FileStore.
     * 
     * @throws FileStoreException  If the FileStore could not be started
     */
    public void start() throws FileStoreException {
        if (!isRunning) {
            System.out.println("FileStore: Starting");
            
            try {
                // Create data directory.
                File dir = new File(dataDirectory);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            } catch (Exception e) {
                throw new FileStoreException(
                        "Could not create data directory: " + dataDirectory, e);
            }
            
            try {
                // Read index file.
                readIndexFile();
            } catch (IOException e) {
                throw new FileStoreException(
                        "Could not read index file: " + INDEX_FILE, e);
            }
            
            try {
                // Open data file.
                dataFile = new RandomAccessFile(
                        dataDirectory + '/' + DATA_FILE, "rw");
            } catch (IOException e) {
                throw new FileStoreException(
                        "Could not open data file: " + DATA_FILE, e);
            }

            isRunning = true;
            
            System.out.println("FileStore: Started");
        } else {
            System.err.println("WARNING: Already running.");
        }
    }
    

    /**
     * Shuts down the FileStore.
     * 
     * @throws FileStoreException  If the FileStore could not be shut down
     */
    public void shutdown() throws FileStoreException {
        if (isRunning) {
            System.out.println("FileStore: Shutting down");
            
            sync();
            
            try {
                dataFile.close();
            } catch (IOException e) {
                throw new FileStoreException("Error closing data file", e);
            } finally {
                isRunning = false;
            }
            System.out.println("FileStore: Shut down");
        } else {
            throw new FileStoreException("FileStore not started");
        }
    }
    

    public int size() {
        return entries.size();
    }
    

    public byte[] retrieve(int id) throws FileStoreException {
        byte[] data = null;
        if (isRunning) {
            System.out.println("FileStore: Retrieving entry with ID " + id);
            FileEntry entry = entries.get(id);
            if (entry != null) {
                data = new byte[entry.getLength()];
                try {
                    dataFile.seek(entry.getOffset());
                    dataFile.read(data);
                } catch (IOException e) {
                    throw new FileStoreException(
                            "Could not read entry with ID " + id);
                }
            } else {
                throw new FileStoreException(
                        "Entry with ID " + id + " not found");
            }
        } else {
            throw new FileStoreException("FileStore not started");
        }
        return data;
    }
    

    /**
     * Stores an entry based on an byte array.
     * 
     * @param id    the entry ID
     * @param data  the data
     * @throws FileStoreException  If the entry could not be stored
     */
    public void store(int id, byte[] data)
            throws FileStoreException {
        if (isRunning) {
            System.out.println("FileStore: Storing entry with ID " + id);
            FileEntry entry = entries.get(id);
            if (entry == null) {
                // Insert new entry.
                
                // Find a free position.
                int offset = findFreePosition(data.length);
                System.out.println("FileStore: Creating new entry with offset " + offset + " and length " + data.length + "...");
                
                // Create new index entry.
                entry = new FileEntry(id);
                entry.setOffset(offset);
                entry.setLength(data.length);
                entries.put(id, entry);
                
                // Write entry.
                try {
                    dataFile.seek(offset);
                    dataFile.write(data);
                    sync();
                } catch (IOException e) {
                    entries.remove(entry);
                    throw new FileStoreException("Error updating data file", e);
                }
            } else {
                // Update existing entry.
                if (data.length <= entry.getLength()) {
                    // Fits; overwrite same entryition.
                    System.out.println("FileStore: Updating existing entry with offset " + entry.getOffset() + " and length " + data.length + "...");

                    // Update length.
                    entry.setLength(data.length);
                    
                    // Write data.
                    try {
                        dataFile.seek(entry.getOffset());
                        dataFile.write(data);
                        sync();
                    } catch (IOException e) {
                        throw new FileStoreException("Error updating data file", e);
                    }
                } else {
                    // Does not fit; delete old entry.
                    entries.remove(entry);
                    
                    // Find a free entryition.
                    int offset = findFreePosition(data.length);
                    
                    System.out.println("FileStore: Moving existing entry with offset " + offset + " and length " + data.length + "...");
                    
                    // Update index entry.
                    entry.setOffset(offset);
                    entry.setLength(data.length);
                    entries.put(id, entry);
                    
                    // Write entry.
                    try {
                        dataFile.seek(offset);
                        dataFile.write(data);
                        sync();
                    } catch (IOException e) {
                        throw new FileStoreException("Error updating data file", e);
                    }
                }
            }
        } else {
            throw new FileStoreException("FileStore not started");
        }
    }
    

    public boolean contains(int id) {
        return entries.containsKey(id);
    }
    

    public void delete(int id) {
        if (isRunning) {
            FileEntry entry = entries.get(id);
            if (entry != null) {
                entries.remove(entry);
                System.out.println("FileStore: Deleted entry with ID '" + id + "' with offset " + entry.getOffset() + " and length " + entry.getLength());
            } else {
                System.err.println("FileStore: WARNING: Entry with ID " + id + " not found");
            }
        } else {
            System.err.println("ERROR: Not started.");
        }
    }
    

    public void deleteAll() {
        entries.clear();
        sync();
        try {
            dataFile.setLength(0L);
        } catch (IOException e) {
            System.err.println("FileStore: ERROR clearing data file: " + e);
        }
        System.out.println("FileStore: Deleted all entries.");
    }
    

    public void sync() {
      System.out.println("FileStore: Sync'ing to disk...");
      try {
          writeIndexFile();
      } catch (IOException e) {
          System.err.println("FileStore: ERROR sync'ing to disk: " + e);
      }
  }
  

    public void printSizeInfo() {
        long stored = getStoredSpace();
        long used = getUsedSpace();
        long wasted = stored - used;
        double wastedPerc = 0.0;
        if (stored > 0) {
            wastedPerc = ((double) wasted / (double) stored) * 100.0;
        }
        System.out.println(String.format(
        		"FileStore: Disk usage:  Size: %s, Used: %s, Wasted: %s (%.1f %%)",
                diskSizeToString(stored), diskSizeToString(used),
                diskSizeToString(wasted), wastedPerc));
    }
    

    //------------------------------------------------------------------------
    //  Private methods
    //------------------------------------------------------------------------
    

    private int findFreePosition(int length) {
        int offset = 0;
        for (FileEntry entry : entries.values()) {
            // Determine any free space between entries.
            long free = entry.getOffset() - offset;
            if (free >= length) {
                // Found a free spot!
                break;
            } else {
                // Proceed to next entry.
                offset = entry.getOffset() + entry.getLength();
            }
        }
        return offset;
    }
    
    
    private void readIndexFile() throws IOException {
        entries.clear();
        File file = new File(dataDirectory + '/' + INDEX_FILE);
        if (file.exists()) {
            DataInputStream dis =
                    new DataInputStream(new FileInputStream(file));
            int noOfEntries = dis.readInt();
            for (int i = 0; i < noOfEntries; i++) {
                int id = dis.readInt();
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
    

    private void writeIndexFile() throws IOException {
        DataOutputStream dos = new DataOutputStream(
                new FileOutputStream(dataDirectory + '/' + INDEX_FILE));
        dos.writeInt(entries.size());
        for (FileEntry entry : entries.values()) {
            dos.writeInt(entry.getId());
            dos.writeInt(entry.getOffset());
            dos.writeInt(entry.getLength());
        }
        dos.close();
    }
    

    private String diskSizeToString(long size) {
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
    

    private long getStoredSpace() {
        long size = 0L;
        try {
            size = dataFile.length();
        } catch (IOException e) {
            System.err.println(e);
        }
        return size;
    }
    

    private long getUsedSpace() {
        long size = 0L;
        for (FileEntry entry : entries.values()) {
            size += entry.getLength();
        }
        return size;
    }
    

}
