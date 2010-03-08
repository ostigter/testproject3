package org.ozsoft.courier.connector;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.ozsoft.courier.NamespaceResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Handler that polls for files in a specific directory.
 * 
 * @author Oscar Stigter
 */
public class FilePoller extends Connector implements Runnable {
    
	/** The log. */
	private static final Logger LOG = Logger.getLogger(FilePoller.class);
    
	/** The directory to watch for new files. */
	private final File dir;
	
    /** The polling interval in miliseconds. */
	private final long interval;
	
	/** Files currently being processed. */
    private final Set<File> openFiles;
    
    /** XML document builder. */
    private DocumentBuilder docBuilder;
    
    /**
	 * Constructor.
	 * 
	 * @param path
	 *            Path of the directory to poll for new files.
	 * @param interval
	 *            The polling interval in miliseconds.
	 */
    public FilePoller(String path, long interval, NamespaceResolver namespaceResolver) {
    	super(namespaceResolver);
        this.interval = interval;
        
        LOG.debug(String.format("Configuring file-in handler with path '%s'", path));
        
        dir = new File(path);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("Directory not found: " + path);
        }
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            docBuilder = dbf.newDocumentBuilder();
        } catch (Exception e) {
            throw new RuntimeException("Error instantiating DocumentBuilder", e);
        }

        openFiles = new HashSet<File>();
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        while (isRunning()) {
            try {
                pollForFiles();
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
    }
    
    /**
     * Polls for new files to process.
     */
    private void pollForFiles() {
        for (File file : dir.listFiles()) {
            if (file.isFile() && !openFiles.contains(file)) {
                processFile(file);
            }
        }
    }
    
    /**
     * Processes a file.
     * 
     * @param file
     *            The file.
     */
    private void processFile(File file) {
        openFiles.add(file);
        
        LOG.debug(String.format("Received file '%s'", file));
        
        try {
            Document doc = docBuilder.parse(file);
            Node message = doc.getFirstChild();
            if (message != null) {
                handleMessage(message);
            }
        } catch (Exception e) {
            LOG.error(String.format("Error parsing file '%s'", file), e);
        }

        boolean isDeleted = file.delete();
        if (!isDeleted) {
            LOG.error(String.format("Could not delete file '%s'", file));
        }
        
        openFiles.remove(file);
    }
    
}
