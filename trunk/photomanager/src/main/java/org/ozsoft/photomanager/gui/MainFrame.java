package org.ozsoft.photomanager.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.ozsoft.photomanager.entities.Album;
import org.ozsoft.photomanager.services.PhotoService;

public class MainFrame extends JFrame {

    private static final int WIDTH = 900;

    private static final int HEIGHT = 600;

    private static final long serialVersionUID = 5613649607058463832L;

    private static final Logger LOGGER = Logger.getLogger(MainFrame.class);

    private final PhotoService photoService;

    private final JButton createAlbumButton;

    private final AlbumPanel albumPanel;

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

        createAlbumButton = new JButton("Create Album");
        createAlbumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createAlbum();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 5, 10);
        getContentPane().add(createAlbumButton, gbc);

        albumPanel = new AlbumPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 10, 10, 10);
        JScrollPane scrollPane = new JScrollPane(albumPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        getContentPane().add(scrollPane, gbc);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                albumPanel.revalidate();
            }
        });

        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);

        LOGGER.info("Started");

        createAlbum("01.jpg", "Album 1");
        createAlbum("02.jpg", "Album 2");
        createAlbum("03.jpg", "Album 3");
//        createAlbum("04.jpg", "Album 4");
//        createAlbum("05.jpg", "Album 5");
//        createAlbum("06.jpg", "Album 6");
    }

    private void createAlbum(String filename, String name) {
        Album album = new Album();
        album.setName(name);
        album.setDate(new Date());
        photoService.storeAlbum(album);
        if (!name.equals("Album 3")) {
            photoService.uploadPhotos(album, new File[] { new File(filename) });
        }
        albumPanel.addAlbum(album);
        albumPanel.revalidate();
    }

    private void createAlbum() {
        AlbumPropertiesDialog dialog = new AlbumPropertiesDialog(this);
        if (dialog.show() == Dialog.OK) {
            Album album = new Album();
            album.setName(dialog.getName());
            album.setDate(dialog.getDate());
            photoService.storeAlbum(album);
            albumPanel.addAlbum(album);
        }

        // JFileChooser fileChooser = new JFileChooser();
        // fileChooser.setFileFilter(new FileNameExtensionFilter("Image files",
        // "jpg", "gif", "png"));
        // fileChooser.setAcceptAllFileFilterUsed(false);
        // fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        // fileChooser.setMultiSelectionEnabled(true);
        // // TODO: Remember recently used directory.
        // fileChooser.setCurrentDirectory(new File("."));
        // if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        // {
        // Album album = new Album();
        // album.setName("Test album");
        // album.setDate(new Date());
        // photoService.storeAlbum(album);
        // photoService.uploadPhotos(album, fileChooser.getSelectedFiles());
        // albumPanel.addAlbum(album);
        // }
    }

    private void close() {
        dispose();
        LOGGER.info("Closed");
    }

}
