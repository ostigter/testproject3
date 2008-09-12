package xantippefiller;


import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import xantippe.Collection;
import xantippe.Database;
import xantippe.DatabaseImpl;
import xantippe.Document;
import xantippe.XmldbException;


/**
 * Application to insert a large amount of files into an embedded Xantippe
 * database in one operation.
 * 
 * Accepts one or more ZIP files containing up to 64k files each (due to a
 * limitation in Java up to version 1.5).
 * 
 * Offers a Swing UI with progress bar and file logging. The insertion can be
 * canceled at anytime by the user.
 *
 * @author Oscar Stigter
 */
public class XantippeFiller extends JFrame {
    

    private static final long serialVersionUID = -1L;
    
    private static final String APPLICATION_TITLE = "Xantippe XML DB Filler";
    
    private static final String LOG_FILE = "XantippeFiller.log";
    
    private static final int BUFFER_SIZE = 8192;  // 8 kB
    
    private static final String DEFAULT_BROWSE_DIR =
            "C:/LocalData/Temp/XML_docs/Compressed";
    
    private static final String DEFAULT_COLLECTION = "/db/data";

    private static final SimpleDateFormat dateFormatter  =
            new SimpleDateFormat("[dd-MM-yyyy HH:mm:ss] ");
    
    private static final Font FONT = new Font("Courier New", Font.PLAIN, 12);

    private final byte[] buffer = new byte[BUFFER_SIZE];
    
    private BufferedWriter logWriter;
    private Database database;
    private String collection = DEFAULT_COLLECTION;
    private int documentCount;
    private boolean isRunning = false;
    private boolean isCanceled;
    private int progress;
    private long startTime;
    private long totalSize;
    private double speed;
    
    // UI components.
    private JFileChooser chooser;
    private DefaultListModel listModel;
    private JList list;
    private JButton addButton;
    private JButton removeButton;
    private JTextField countField;
    private JTextField collectionField;
    private JButton startButton;
    private JButton cancelButton;
    private JButton closeButton;
    private JProgressBar progressBar;
    private JTextField durationField;
    private JTextField progressField;
    private JTextField fileField;
    private JTextField speedField;
    

    /**
     * Constructor.
     */
    public XantippeFiller() {
        super(APPLICATION_TITLE);
        
        createGUI();
        updateDocumentCount();
        
        log("Application started.");
    }
    
    /**
     * Application's main entry point.
     *
     * @param  args  command-line arguments
     */
    public static void main(String[] args) {
        new XantippeFiller();
    }
    
    /**
     * Creates the Swing UI.
     */
    private void createGUI() {
        // Always catch the closing request.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (!isRunning) {
                    close();
                }
            }
        });
        
        GridBagConstraints gc = new GridBagConstraints();
        
        listModel = new DefaultListModel();
        list = new JList(listModel);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browse();
            }
        });
        
        removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                remove();
            }
        });
        
        chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new ZipFileFilter());
        chooser.setMultiSelectionEnabled(true);
        chooser.setCurrentDirectory(new File(DEFAULT_BROWSE_DIR));
        
        JLabel countLabel = new JLabel("Number of files:");
        
        countField = new JTextField();
        countField.setFont(FONT);
        countField.setEditable(false);
        countField.setHorizontalAlignment(JTextField.CENTER);
        
        JLabel collectionLabel = new JLabel("Collection:");
        
        collectionField = new JTextField();
        collectionField.setFont(FONT);
        collectionField.setText(collection);
        
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        
        cancelButton = new JButton("Cancel");
        cancelButton.setEnabled(false);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        
        durationField = new JTextField();
        durationField.setEditable(false);
        durationField.setFont(FONT);
        durationField.setHorizontalAlignment(JTextField.CENTER);
        
        progressField = new JTextField();
        progressField.setEditable(false);
        progressField.setFont(FONT);
        progressField.setHorizontalAlignment(JTextField.CENTER);
        
        fileField = new JTextField();
        fileField.setEditable(false);
        fileField.setFont(FONT);
        fileField.setHorizontalAlignment(JTextField.CENTER);
        
        speedField = new JTextField(10);
        speedField.setEditable(false);
        speedField.setFont(FONT);
        speedField.setHorizontalAlignment(JTextField.CENTER);
        
        setLayout(new GridBagLayout());
        
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 3;
        gc.gridheight = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.VERTICAL;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 10, 5, 10);
        getContentPane().add(scrollPane, gc);
        gc.gridx = 3;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.SOUTH;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 5, 5, 10);
        getContentPane().add(addButton, gc);
        gc.gridx = 3;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.NORTH;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 10, 10);
        getContentPane().add(removeButton, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 10, 5, 10);
        getContentPane().add(countLabel, gc);
        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 10);
        getContentPane().add(countField, gc);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 10, 5, 5);
        getContentPane().add(collectionLabel, gc);
        gc.gridx = 1;
        gc.gridy = 3;
        gc.gridwidth = 3;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 10);
        getContentPane().add(collectionField, gc);
        gc.gridx = 0;
        gc.gridy = 4;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.EAST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 10, 5, 5);
        getContentPane().add(startButton, gc);
        gc.gridx = 1;
        gc.gridy = 4;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 10);
        getContentPane().add(cancelButton, gc);
        gc.gridx = 3;
        gc.gridy = 4;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.EAST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 10, 5, 10);
        getContentPane().add(closeButton, gc);
        gc.gridx = 0;
        gc.gridy = 5;
        gc.gridwidth = 4;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 10, 5, 10);
        getContentPane().add(progressBar, gc);
        gc.gridx = 0;
        gc.gridy = 6;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 10, 5, 5);
        getContentPane().add(durationField, gc);
        gc.gridx = 1;
        gc.gridy = 6;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(progressField, gc);
        gc.gridx = 2;
        gc.gridy = 6;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(fileField, gc);
        gc.gridx = 3;
        gc.gridy = 6;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 10, 10);
        getContentPane().add(speedField, gc);
        
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Shows a standard Open dialog to select the archive files to process.
     */
    private void browse() {
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            for (File file : chooser.getSelectedFiles()) {
                if (file.exists() && file.canRead()) {
                    add(file);
                }
            }
        }
    }
    
    /**
     * Adds an archive file to the listbox.
     * Afterwards the file count is updated.
     *
     * @param  file  the archive file
     */
    private void add(File file) {
        listModel.addElement(file);
        updateDocumentCount();
    }
    
    /**
     * Removes the selected archive file from the listbox.
     * Afterwards the file count is updated.
     */
    private void remove() {
        int index = list.getSelectedIndex();
        if (index != -1) {
            listModel.remove(index);
            updateDocumentCount();
        }
    }
    
    /**
     * Updates the file count by scanning the archive files.
     */
    private void updateDocumentCount() {
        // TODO: Use caching.
        // TODO: Use worker thread to avoid the UI from freezing.
        documentCount = 0;
        int len = listModel.size();
        for (int i = 0; i < len; i++) {
            File file = (File) listModel.get(i);
            documentCount += getArchiveFileCount(file);
        }
        countField.setText(String.valueOf(documentCount));
        startButton.setEnabled(documentCount != 0);
    }
    

    /**
     * Returns the number of files inside the specified archive file.
     *
     * @return  the number of files
     */
    private int getArchiveFileCount(File file) {
        int count = 0;
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (!entry.isDirectory()) {
                    count++;
                }
            }
            zipFile.close();
        } catch (IOException ex) {
            System.err.println("*** ERROR: " + ex);
        }
        return count;
    }
    

    /**
     * Starts the insertion operation.
     */
    private void start() {
        collection = collectionField.getText();
        if (!collection.startsWith("/db")) {
            JOptionPane.showMessageDialog(this,
                    "The specified collection is illegal.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        startButton.setEnabled(false);
        addButton.setEnabled(false);
        removeButton.setEnabled(false);
        closeButton.setEnabled(false);
        cancelButton.setEnabled(true);
        
        Thread thread = new Thread() {
            public void run() {
                progressBar.setMaximum(documentCount);
                progress = 0;
                setProgress(progress);
                isRunning = true;
                isCanceled = false;
                totalSize = 0L;
                startTime = System.currentTimeMillis();

                try {
                    startDatabase();
                    storeDocuments();
                } catch (XmldbException ex) {
                    log("*** ERROR: " + ex);
                }

                shutdownDatabase();

                cancelButton.setEnabled(false);
                startButton.setEnabled(true);
                addButton.setEnabled(true);
                removeButton.setEnabled(true);
                closeButton.setEnabled(true);
                
                isRunning = false;
            }
        };
        thread.start();
    }
    

    /**
     * Starts the embedded database.
     *
     * @throws  XmldbException  if the database could not be started
     */
    private void startDatabase() throws XmldbException {
        if (database == null) {
            log("Starting database...");
            
            database = new DatabaseImpl();
            database.start();
            database.getRootCollection().createCollection("data");
            
            log("Database started.");
        }
    }
    

    /**
     * Shuts down the embedded database.
     */
    private void shutdownDatabase() {
        if (database != null) {
            log("Shutting down database...");
            try {
                database.shutdown();
                log("Database shut down.");
            } catch (XmldbException e) {
                log("ERROR: " + e.getMessage());
            } finally {
                database = null;
            }
        }
    }
    

    /**
     * Inserts the files inside the archive files in the database.
     *
     * @throws  XmldbException  if the target collection does not exist
     */
    private void storeDocuments() throws XmldbException {
        if (database != null) {
            log("Start storing documents...");
            
            Collection col = database.getCollection(collection);
            
            int len = listModel.size();
            for (int i = 0; (i < len) && !isCanceled; i++) {
                File file = (File) listModel.get(i);
                log("Processing archive '" + file.getPath() + "'...");
                try {
                    ZipFile zipFile = new ZipFile(file);
                    Enumeration<?> entries = zipFile.entries();
                    while (entries.hasMoreElements() && !isCanceled) {
                        ZipEntry entry = (ZipEntry) entries.nextElement();
                        if (!entry.isDirectory()) {
                            storeFileEntry(zipFile, entry, col);
                            setProgress(progress + 1);
                            totalSize += entry.getSize();
                            long duration = (System.currentTimeMillis() - startTime) / 1000;
                            speed = ((double) totalSize / 1024) / (double) duration;
                            durationField.setText(getDurationString(duration));
                            speedField.setText(String.format("%.0f kB/s", speed));
                        }
                    }
                    zipFile.close();
                } catch (IOException e) {
                    log("*** ERROR while processing archive '"
                            + file.getPath() + "': " + e);
                }
            }
            long duration = (System.currentTimeMillis() - startTime) / 1000;
            if (isCanceled) {
                log("Operation canceled by the user.");
            } else {
                log("Finished storing documents.");
            }
            log(String.format("File count: %d", documentCount));
            log(String.format("File size:  %s", getSizeString(totalSize)));
            log(String.format("Duration:   %s hrs", getDurationString(duration)));
            log(String.format("Speed:      %.0f kB/s.", speed));
        }
    }
    

    /**
     * Inserts a file from inside an archive file in the database.
     *
     * @param  file   the archive file
     * @param  entry  the file to be inserted
     */
    private void storeFileEntry(ZipFile file, ZipEntry entry, Collection col) {
        String fileName = getShortFilename(entry);
        log("Storing document '" +  fileName + "'...");
        fileField.setText(fileName);
        try {
            InputStream is = file.getInputStream(entry);
            Document doc = col.createDocument(entry.getName());
            OutputStream os = doc.setContent();
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            log("*** ERROR while storing document '" + fileName + "': " + e);
        }
    }
    

    /**
     * Sets the progress to the specified value.
     *
     * @param  progress  the new progress value
     */
    private void setProgress(int progress) {
        this.progress = progress;
        progressField.setText(String.valueOf(progress));
        progressBar.setValue(progress);
    }
    

    /**
     * Returns the short filename of the specified ZIP file entry.
     *
     * @return  the short filename
     */
    private String getShortFilename(ZipEntry entry) {
        String name = entry.getName();
        int pos = name.lastIndexOf('/');
        if (pos != -1) {
            name = name.substring(pos + 1);
        }
        return name;
    }
    

    /**
     * Cancels the insertion operation.
     */
    private void cancel() {
        isCanceled = true;
    }
    

    /**
     * Writes the specified message to the application's log file.
     *
     * @param  message  the log message
     */
    private synchronized void log(String message) {
        message = dateFormatter.format(Calendar.getInstance().getTime()) + message;
        try {
            if (logWriter == null) {
                logWriter = new BufferedWriter(new FileWriter(LOG_FILE));
            }
            logWriter.write(message);
            logWriter.newLine();
            logWriter.flush();
        } catch (IOException ex) {
            System.err.println("*** ERROR: Could not write to log file '" + LOG_FILE + "'.");
        }
    }
    

    /**
     * Returns a string representation of the specified duration in seconds,
     * in the format 'hh:mm:ss'.
     *
     * @param   duration  the duration in seconds
     * @return  the string representation
     */
    private static String getDurationString(long duration) {
        int hour = (int) (duration / 3600);
        int min  = (int) ((duration - (hour * 3600)) / 60);
        int sec  = (int) (duration - (hour * 3600) - (min * 60));
        return String.format("%d:%02d:%02d", hour, min, sec);
    }
    

    /**
     * Returns a human-readable string representation of the specified file
     * size, like "123 bytes", "1.23 kB", "1.23 MB" and "1.23 GB".
     *
     * @param   size  the file size in bytes
     * @return  the string representation
     */
    private static String getSizeString(long size) {
        String s = null;
        if (size < 1024) {
            s = String.format("%d bytes", size);
        } else if (size < 1048576) {
            s = String.format("%.2f kB", ((double) size) / 1024);
        } else if (size < 1073741824) {
            s = String.format("%.2f MB", ((double) size) / 1048576);
        } else {
            s = String.format("%.2f GB", ((double) size) / 1073741824);
        }
        return s;
    }
    

    /**
     * Closes the application.
     */
    private void close() {
        shutdownDatabase();
        log("Application closed.");
        dispose();
    }
    
    
    //------------------------------------------------------------------------
    //  Inner classes
    //------------------------------------------------------------------------


    /**
     * File filter for ZIP archive files (*.zip).
     */
    public class ZipFileFilter extends FileFilter {
        
        
        public boolean accept(File file) {
            return (file.isDirectory() || file.getName().endsWith(".zip"));
        }
        
        
        public String getDescription() {
            return "Archive files (*.zip)";
        }
        

    } // ZipFileFilter


} // XantippeFiller
