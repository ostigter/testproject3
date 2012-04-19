package org.ozsoft.photomanager.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.log4j.Logger;
import org.ozsoft.photomanager.entities.Album;
import org.ozsoft.photomanager.services.PhotoService;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 5613649607058463832L;
    
    private static final Logger LOGGER = Logger.getLogger(MainFrame.class);
    
    private final PhotoService photoService;

    private JButton uploadButton;

    public MainFrame() {
        super("PhotoManager");
        
        photoService = new PhotoService();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        getContentPane().setLayout(new GridBagLayout());
        
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
        getContentPane().add(uploadButton, gbc);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        
        LOGGER.info("Started");
    }

    private void uploadPhotos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "gif", "png"));
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        //TODO: Remember recently used directory.
        fileChooser.setCurrentDirectory(new File("D:/Pictures"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            Album album = new Album();
            album.setName("Test album");
            album.setDate(new Date());
            photoService.storeAlbum(album);
            photoService.uploadPhotos(album, fileChooser.getSelectedFiles());
        }
    }

    private void close() {
        dispose();
        LOGGER.info("Closed");
    }

}
