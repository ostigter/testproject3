package xen.filestore;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;


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
    

	private static final String DEFAULT_DATA_DIR = "data";
	
    private static final String INDEX_FILE       = "index.dbx";
    
    private static final String DATA_FILE        = "data.dbx";
    
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private final TreeSet<FileEntry> positions;
    
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
        positions = new TreeSet<FileEntry>();
    }
    

    //------------------------------------------------------------------------
    //  Public methods
    //------------------------------------------------------------------------
    

    /**
     * Starts the FileStore.
     * 
     * @throws FileStoreException  If the FileStore could not be started
     */
    public synchronized void start() throws FileStoreException {
        if (!isRunning) {
//            System.out.println("FileStore: Starting...");
            
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
            
//            System.out.println("FileStore: Started successfully.");
        } else {
            System.err.println("WARNING: Already running.");
        }
    }
    

    /**
     * Shuts down the FileStore.
     * 
     * @throws FileStoreException  If the FileStore could not be shut down
     */
    public synchronized void shutdown() throws FileStoreException {
        if (isRunning) {
//            System.out.println("FileStore: Shutting down...");
            
            sync();
            
            try {
                dataFile.close();
            } catch (IOException e) {
                throw new FileStoreException("Error closing data file", e);
            } finally {
                isRunning = false;
            }
//            System.out.println("FileStore: Shut down successfully.");
        } else {
            throw new FileStoreException("FileStore not started");
        }
    }
    

    public synchronized int size() {
        return positions.size();
    }
    

    public synchronized String[] getFileNames() {
        List<String> list = new ArrayList<String>(size());
        for (FileEntry pos : positions) {
            list.add(pos.getName());
        }
        return list.toArray(EMPTY_STRING_ARRAY);
    }
    

    public synchronized void list() {
        System.out.println("Entries:");
        if (positions.size() > 0) {
            for (FileEntry pos : positions) {
                System.out.println(String.format(
                        "  %s (%d bytes)", pos.getName(), pos.getLength()));
            }
        } else {
            System.out.println("  (None)");
        }
    }
    

    public synchronized byte[] retrieve(String name)
            throws FileStoreException {
        byte[] data = null;
        if (isRunning) {
            FileEntry pos = getPosition(name);
            if (pos != null) {
//                System.out.println("FileStore: Retrieving '" + name + "'...");
                data = new byte[(int) pos.getLength()];
                try {
                    dataFile.seek(pos.getOffset());
                    dataFile.read(data);
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                    throw new FileStoreException("Could not read file '" + name + "'");
                }
            } else {
                throw new FileStoreException("File '" + name + "' not found");
            }
        } else {
            throw new FileStoreException("FileStore not started");
        }
        return data;
    }
    

    /**
     * Stores an entry based on an byte array.
     * 
     * @param name  the entry name
     * @param data  the data
     * @throws FileStoreException  If the entry could not be stored
     */
    public synchronized void store(String name, byte[] data)
            throws FileStoreException {
        if (isRunning) {
            System.out.println("FileStore: Storing '" + name + "'...");
            FileEntry pos = getPosition(name);
            if (pos == null) {
                // Insert new entry.
                
                // Find a free position.
                long offset = findFreePosition(data.length);
//                System.out.println("FileStore: Creating new entry with offset " + offset + " and length " + data.length + "...");
                
                // Create new index entry.
                pos = new FileEntry(name);
                pos.setOffset(offset);
                pos.setLength(data.length);
                positions.add(pos);
                
                // Write entry.
                try {
                    dataFile.seek(offset);
                    dataFile.write(data);
                    sync();
                } catch (IOException e) {
                    positions.remove(pos);
                    throw new FileStoreException("Error updating data file", e);
                }
            } else {
                // Update existing entry.
                if (data.length <= pos.getLength()) {
                    // Fits; overwrite same position.
//                    System.out.println("FileStore: Updating existing entry with offset " + pos.getOffset() + " and length " + data.length + "...");

                    // Update length.
                    pos.setLength(data.length);
                    
                    // Write data.
                    try {
                        dataFile.seek(pos.getOffset());
                        dataFile.write(data);
                        sync();
                    } catch (IOException e) {
                        throw new FileStoreException("Error updating data file", e);
                    }
                } else {
                    // Does not fit; delete old entry.
                    positions.remove(pos);
                    
                    // Find a free position.
                    long offset = findFreePosition(data.length);
                    
//                    System.out.println("FileStore: Moving existing entry with offset " + offset + " and length " + data.length + "...");
                    
                    // Update index entry.
                    pos.setOffset(offset);
                    pos.setLength(data.length);
                    positions.add(pos);
                    
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
    

    public boolean contains(String name) {
        return (getPosition(name) != null);
    }
    

    public synchronized void delete(String name) {
        if (isRunning) {
            FileEntry pos = getPosition(name);
            if (pos != null) {
                positions.remove(pos);
//                System.out.println("FileStore: Deleted '" + name + "' with offset " + pos.getOffset() + " and length " + pos.getLength());
            } else {
                System.err.println("FileStore: WARNING: File '" + name + "' not found.");
            }
        } else {
            System.err.println("ERROR: Not started.");
        }
    }
    

    public synchronized void deleteAll() {
        positions.clear();
        sync();
        try {
            dataFile.setLength(0L);
        } catch (IOException e) {
            System.err.println("FileStore: ERROR clearing data file: " + e);
        }
//        System.out.println("FileStore: Deleted all entries.");
    }
    

    public synchronized void sync() {
//      System.out.println("FileStore: Sync'ing to disk...");
      try {
          writeIndexFile();
      } catch (IOException e) {
          System.err.println("FileStore: ERROR sync'ing to disk: " + e);
      }
  }
  

    public synchronized void printSizeInfo() {
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
    

    private FileEntry getPosition(String name) {
        FileEntry foundPos = null;
        for (FileEntry pos : positions) {
            if (pos.getName().equals(name)) {
                foundPos = pos;
                break;
            }
        }
        return foundPos;
    }
    
    
    private long findFreePosition(long length) {
        long offset = 0L;
        for (FileEntry pos : positions) {
            // Determine any free space between positions.
            long free = pos.getOffset() - offset;
            if (free >= length) {
                // Found a free spot!
                break;
            } else {
                // Proceed to next position.
                offset = pos.getOffset() + pos.getLength();
            }
        }
        return offset;
    }
    
    
    private void readIndexFile() throws IOException {
        positions.clear();
        File file = new File(dataDirectory + '/' + INDEX_FILE);
        if (file.exists()) {
            DataInputStream dis =
                    new DataInputStream(new FileInputStream(file));
            int noOfEntries = dis.readInt();
            for (int i = 0; i < noOfEntries; i++) {
                String name = dis.readUTF();
                long offset = dis.readLong();
                long length = dis.readLong();
                FileEntry pos = new FileEntry(name);
                pos.setOffset(offset);
                pos.setLength(length);
                positions.add(pos);
            }
            dis.close();
        }
    }
    

    private void writeIndexFile() throws IOException {
        DataOutputStream dos = new DataOutputStream(
                new FileOutputStream(dataDirectory + '/' + INDEX_FILE));
        dos.writeInt(positions.size());
        for (FileEntry pos : positions) {
            String name = pos.getName();
            dos.writeUTF(name);
            dos.writeLong(pos.getOffset());
            dos.writeLong(pos.getLength());
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
        for (FileEntry pos : positions) {
            size += pos.getLength();
        }
        return size;
    }
    

}
