package org.ozsoft.mediacenter;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public abstract class MediaPanel extends JPanel implements LibraryListener {

    private static final long serialVersionUID = 1L;

    private final Configuration config;

    protected final Library library;

    private JList<MediaItem> list;

    private JButton deleteButton;

    private DefaultListModel<MediaItem> listModel;

    protected MediaFolder currentFolder;

    public MediaPanel(Configuration config, Library library) {
        this.config = config;
        this.library = library;
        library.addListener(this);
        createUI();
    }

    @Override
    public abstract void libraryUpdated();

    protected void updateList() {
        listModel.clear();

        // First add a phony parent folder (if any)...
        MediaFolder parent = currentFolder.getParent();
        if (parent != null) {
            listModel.addElement(new MediaFolder(Constants.PARENT_FOLDER_NAME, parent.getPath(), parent));
        }
        // ...then add any subfolders...
        for (MediaFolder folder : currentFolder.getFolders()) {
            listModel.addElement(folder);
        }
        // ...then any files.
        for (MediaFile file : currentFolder.getFiles()) {
            listModel.addElement(file);
        }
    }

    @SuppressWarnings("unchecked")
    private void createUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        listModel = new DefaultListModel<MediaItem>();

        list = new JList<MediaItem>(listModel);
        list.setBackground(Constants.BACKGROUND);
        list.setForeground(Constants.FOREGROUND);
        list.setCellRenderer(new MediaCellRenderer(library));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                deleteButton.setEnabled(list.getSelectedIndex() != -1);
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    activateListItem();
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(list);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(scrollPane, gbc);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(Constants.FONT);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                library.update();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        add(refreshButton, gbc);

        deleteButton = new JButton("Delete");
        deleteButton.setFont(Constants.FONT);
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        add(deleteButton, gbc);
    }

    private void activateListItem() {
        Object value = list.getSelectedValue();
        if (value instanceof MediaFolder) {
            MediaFolder folder = (MediaFolder) value;
            if (folder.getName().equals(Constants.PARENT_FOLDER_NAME)) {
                currentFolder = folder.getParent();
            } else {
                currentFolder = folder;
            }
            updateList();
        } else {
            playMedia((MediaFile) value);
        }
    }

    private void delete() {
        Object value = list.getSelectedValue();
        if (value instanceof MediaFile) {
            MediaFile mediaFile = (MediaFile) value;
            File file = new File(mediaFile.getPath());
            if (file.delete()) {
                listModel.removeElement(mediaFile);
                library.deleteShow(mediaFile);
            } else {
                JOptionPane
                        .showMessageDialog(this, "Could not delete media file.\n"
                                + "It is probably being used by another application.", "MediaCenter",
                                JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void playMedia(MediaFile file) {
        ProcessBuilder pb = new ProcessBuilder(config.getPlayerPath(), "/fullscreen", file.getPath());
        Process process = null;
        try {
            process = pb.start();
            long startTime = System.currentTimeMillis();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                long duration = (System.currentTimeMillis() - startTime) / 1000;
                if (duration > Constants.SEEN_DURATION) {
                    library.markFileAsSeen(file.getName());
                }
            } else {
                System.err.println("WARNING: Player exit value: " + exitValue);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Could not start player: " + e);
        }
    }

    // -------------------------------------------------------------------------
    // Inner classes
    // -------------------------------------------------------------------------

    private static class MediaCellRenderer extends JLabel implements ListCellRenderer {

        private static final long serialVersionUID = -1372244827116154566L;

        private final Library library;

        public MediaCellRenderer(Library library) {
            this.library = library;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object obj, int index, boolean isSelected,
                boolean hasFocus) {
            setOpaque(true);
            setBackground(Constants.BACKGROUND);
            setForeground(Constants.FOREGROUND);
            setFont(Constants.FONT);

            Color backColor = Constants.BACKGROUND;
            Color foreColor = Constants.FOREGROUND;
            boolean isSeen = false;

            if (obj instanceof MediaFolder) {
                MediaFolder folder = (MediaFolder) obj;
                setText(String.format("[%s]", folder.getName()));
            } else if (obj instanceof MediaFile) {
                MediaFile file = (MediaFile) obj;
                setText(file.getName());
                isSeen = library.isFileSeen(file.getName());
            } else {
                setText(obj.toString());
            }

            if (isSelected) {
                setForeground(Constants.FOREGROUND_SELECTION);
                setBackground(Constants.BACKGROUND_SELECTION);
            } else {
                setForeground(isSeen ? Constants.FOREGROUND_SEEN : foreColor);
                setBackground(backColor);
            }

            return this;
        }
    }
}
