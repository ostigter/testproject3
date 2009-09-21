package org.ozsoft.mediacenter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.SwingUtilities;

public class Library {
	
	private final Configuration config;
	
	private final MediaFolder showsRoot;
	
    private final MediaFolder moviesRoot;
    
	private final Set<String> seenFiles;
	
	private final Set<LibraryListener> listeners;
	
	public Library(Configuration config) {
		this.config = config;
		showsRoot = new MediaFolder("", "", null);
        moviesRoot = new MediaFolder("", "", null);
		seenFiles = new HashSet<String>();
		listeners = new HashSet<LibraryListener>();
		load();
	}
	
	public void addListener(LibraryListener listener) {
	    listeners.add(listener);
	}
	
	public MediaFolder getShowsRoot() {
		return showsRoot;
	}
	
    public MediaFolder getMoviesRoot() {
        return moviesRoot;
    }
    
	public void update() {
	    final Popup popup = new Popup("Refreshing list...");
	    popup.setVisible(true);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Search for TV shows.
                showsRoot.clear();
                for (String path : config.getShowRoots()) {
                    File dir = new File(path);
                    if (dir.isDirectory() && dir.canRead()) {
                        scanDirectory(dir, showsRoot);
                    } else {
                        throw new RuntimeException("Invalid TV shows root path: " + path);
                    }
                }
                // Search for movies.
                moviesRoot.clear();
                for (String path : config.getMovieRoots()) {
                    File dir = new File(path);
                    if (dir.isDirectory() && dir.canRead()) {
                        scanDirectory(dir, moviesRoot);
                    } else {
                        throw new RuntimeException("Invalid movies root path: " + path);
                    }
                }
                popup.dispose();
                fireListeners();
            }
        });
	}
	
	public boolean isFileSeen(String name) {
	    return seenFiles.contains(name);
	}
	
	public void markFileAsSeen(String name) {
	    seenFiles.add(name);
	}
	
	public void deleteShow(MediaFile file) {
	    showsRoot.deleteFile(file);
	}
	
    public void deleteMovie(MediaFile file) {
        moviesRoot.deleteFile(file);
    }
    
	private void scanDirectory(File dir, MediaFolder folder) {
		// First process directories recusively (depth-first)...
        for (File file : dir.listFiles()) {
            if (file.isDirectory() && file.canRead()) {
            	String name = file.getName().toLowerCase();
            	if (name.charAt(0) != '.') {
                	String path = file.getAbsolutePath();
                	MediaFolder subfolder = new MediaFolder(name, path, folder);
                	scanDirectory(file, subfolder);
                	if (!subfolder.isEmpty()) {
                        folder.addFolder(subfolder);
                	}
            	}
            }
        }
        // ...then process files in this directory.
        for (File file : dir.listFiles()) {
            if (isVideoFile(file)) {
            	String name = file.getName().toLowerCase();
            	String path = file.getAbsolutePath();
            	folder.addFile(new MediaFile(name, path, folder));
            }
        }
	}
	
	/**
	 * Loads the data file with the list of seen files.
	 */
	private void load() {
	    seenFiles.clear();
	    File file = new File(Constants.DATA_FILE);
	    if (file.isFile()) {
    	    try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String name = null;
                while ((name = reader.readLine()) != null) {
                    seenFiles.add(name);
                }
                reader.close();
            } catch (IOException e) {
                System.err.println("Error reading data file: " + e.getMessage());
            }
	    }
	}
	
	/**
	 * Saves the list of seen files to the data file.
	 */
	public void save() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(Constants.DATA_FILE));
            for (String name : seenFiles) {
                writer.write(name);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing data file: " + e.getMessage());
        }
    }
	
    private static boolean isVideoFile(File file) {
        return file.isFile() && file.getName().endsWith(".avi"); 
    }

//	private void save() {
//		try {
//			DataOutputStream dos = new DataOutputStream(
//					new FileOutputStream(Constants.DATA_FILE));
//			writeFolder(showsRoot, dos);
//			dos.close();
//		} catch (IOException e) {
//			System.err.println("Error writing data file: " + e.getMessage());
//		}
//	}
//	
//	private void writeFolder(MediaFolder folder, DataOutputStream dos)
//			throws IOException {
//		dos.writeUTF(folder.getName());
//		dos.writeUTF(folder.getPath());
//		List<MediaFile> files = folder.getFiles();
//		dos.writeInt(files.size());
//		for (MediaFile f : files) {
//			writeFile(f, dos);
//		}
//		List<MediaFolder> folders = folder.getFolders();
//		dos.writeInt(folders.size());
//		for (MediaFolder f : folders) {
//			writeFolder(f, dos);
//		}
//	}
//
//	private void writeFile(MediaFile file, DataOutputStream dos)
//			throws IOException {
//		dos.writeUTF(file.getName());
//		dos.writeUTF(file.getPath());
//		dos.writeBoolean(file.isSeen());
//	}
	
	private void fireListeners() {
	    for (LibraryListener listener : listeners) {
	        listener.libraryUpdated();
	    }
	}

}
