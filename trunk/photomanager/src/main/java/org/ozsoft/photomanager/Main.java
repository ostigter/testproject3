package org.ozsoft.photomanager;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.ozsoft.photomanager.entities.Album;
import org.ozsoft.photomanager.entities.Photo;
import org.ozsoft.photomanager.services.PhotoService;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;
import com.drew.metadata.jpeg.JpegDirectory;

public class Main {
    
    private static final File DATA_DIR = new File("data");
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("YYYYMMdd");
    
    private static final Logger LOGGER = Logger.getLogger(Main.class);
    
    private PhotoService photoService;

    private JFrame frame;

    private JButton uploadButton;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        LOGGER.info("Started");
        photoService = new PhotoService();
        initUI();
    }

    private void initUI() {
        frame = new JFrame("PhotoManager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        frame.getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        uploadButton = new JButton("Upload Photos");
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadPhotos();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        frame.getContentPane().add(uploadButton, gbc);

        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void uploadPhotos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "gif", "png"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setCurrentDirectory(new File("D:/Pictures"));
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            String albumName = "Test album";
            Date albumDate = new Date();
            Album album = new Album();
            album.setName(albumName);
            album.setDate(albumDate);
            File albumDir = new File(DATA_DIR, String.format("%s - %s", DATE_FORMAT.format(albumDate), albumName));
            albumDir.mkdirs();
            List<Photo> photos = album.getPhotos();
            for (File file : fileChooser.getSelectedFiles()) {
                Photo photo = uploadPhoto(file, albumDir);
                photos.add(photo);
            }
            photoService.storeAlbum(album);
        }
    }

    private Photo uploadPhoto(File sourceFile, File albumDir) {
        LOGGER.debug(String.format("Upload photo from file '%s'", sourceFile.getAbsolutePath()));
        InputStream is = null;
        Photo photo = null;
        try {
            try {
                LOGGER.debug("Extracting EXIF metadata");
                int width = -1;
                int height = -1;
                Date date = null;
                String extension = null;
                int p = sourceFile.getName().lastIndexOf('.');
                if (p >= 0) {
                    extension = sourceFile.getName().substring(p + 1).toLowerCase();
                } else {
                    LOGGER.warn("Uploading image file without extension: " + sourceFile.getAbsolutePath());
                    extension = "jpg";
                }
                Metadata metadata = ImageMetadataReader.readMetadata(new BufferedInputStream(new FileInputStream(sourceFile)));
                Directory dir = metadata.getDirectory(JpegDirectory.class);
                try {
                    if (dir.containsTag(JpegDirectory.TAG_JPEG_IMAGE_WIDTH)) {
                        width = dir.getInt(JpegDirectory.TAG_JPEG_IMAGE_WIDTH);
                        LOGGER.debug("Width: " + width);
                    } else {
                        LOGGER.warn("No width set in EXIF metadata");
                    }
                    if (dir.containsTag(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT)) {
                        height = dir.getInt(JpegDirectory.TAG_JPEG_IMAGE_HEIGHT);
                        LOGGER.debug("Height: " + height);
                    } else {
                        LOGGER.warn("No height set in EXIF metadata");
                    }
                } catch (MetadataException e1) {
                    LOGGER.error(e1);
                }
                dir = metadata.getDirectory(ExifDirectory.class);
                try {
                    if (dir.containsTag(ExifDirectory.TAG_DATETIME_ORIGINAL)) {
                        date = dir.getDate(ExifDirectory.TAG_DATETIME_ORIGINAL);
                    } else {
                        date = new Date(sourceFile.lastModified());
                    }
                    LOGGER.debug("Date: " + date);
                    if (dir.containsTag(ExifDirectory.TAG_ORIENTATION)) {
                        int orientation = dir.getInt(ExifDirectory.TAG_ORIENTATION);
                        LOGGER.debug("Orientation: " + orientation);
                    }
                    
                    // Store metadata in database.
                    photo = new Photo();
                    photo.setWidth(width);
                    photo.setHeight(height);
                    photo.setDate(date);
                    photo.setFileType(extension);
                    photoService.storePhoto(photo);
                    
                    File destFile = new File(albumDir, String.format("P%07d.%s", photo.getId(), extension));
                    LOGGER.debug(String.format("Storing photo content as '%s'", destFile));
                    OutputStream os = null;
                    try {
                        is = new BufferedInputStream(new FileInputStream(sourceFile));
                        os = new BufferedOutputStream(new FileOutputStream(destFile));
                        IOUtils.copy(is, os);
                    } catch (IOException e) {
                        LOGGER.error("Error storing photo content", e);
                    } finally {
                        IOUtils.closeQuietly(is);
                        IOUtils.closeQuietly(os);
                    }
                    
                } catch (MetadataException e1) {
                    LOGGER.error(e1);
                }
            } catch (ImageProcessingException e1) {
                LOGGER.error(e1);
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Failed to upload photo from file '%s'", sourceFile), e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return photo;
    }

    private void close() {
        frame.dispose();
        LOGGER.info("Closed");
    }

}
