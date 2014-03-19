package org.ozsoft.copytool;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class CopyTool {

    private static final String TITLE = "Copy Tool";

    private static final int WIDTH = 800;

    private static final int HEIGHT = 600;

    private static final Font FONT = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    private JFrame frame;

    private JButton addButton;

    private JButton removeButton;
    
    private JList<File> sourcesList;

    private DefaultListModel<File> sourceFiles;

    private JTextField destDirText;

    private JButton browseButton;
    
    private File destDir;

    private JButton copyButton;

    public CopyTool() {
        initUI();
    }

    public static void main(String[] args) {
        new CopyTool();
    }

    private void initUI() {
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel sourcePanel = new JPanel(new GridBagLayout());
        sourcePanel.setBorder(new TitledBorder("Source files and directories"));

        addButton = new JButton("Add...");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSources();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        sourcePanel.add(addButton, gbc);

        removeButton = new JButton("Remove");
        removeButton.setEnabled(false);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSources();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        sourcePanel.add(removeButton, gbc);

        sourceFiles = new DefaultListModel<File>();
        sourcesList = new JList<File>(sourceFiles);
        sourcesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sourcesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                sourcesSelected();
            }
        });
        sourcesList.setFont(FONT);
        JScrollPane scrollPane = new JScrollPane(sourcesList);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        sourcePanel.add(scrollPane, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        frame.getContentPane().add(sourcePanel, gbc);

        JPanel destPanel = new JPanel(new GridBagLayout());
        destPanel.setBorder(new TitledBorder("Destination directory"));

        destDirText = new JTextField();
        destDirText.setFont(FONT);
        destDirText.setBackground(Color.WHITE);
        destDirText.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        destPanel.add(destDirText, gbc);

        browseButton = new JButton("Browse...");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseDestination();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 5, 5);
        destPanel.add(browseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 5, 5, 5);
        frame.getContentPane().add(destPanel, gbc);

        copyButton = new JButton("Copy files");
        copyButton.setEnabled(false);
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyFiles();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 15, 10);
        frame.getContentPane().add(copyButton, gbc);

        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addSources() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setCurrentDirectory(new File("C:/LocalData"));
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            for (File file : fileChooser.getSelectedFiles()) {
                addSourceFile(file);
            }
        }
    }
    
    private void addSourceFile(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                addSourceFile(child);
            }
        } else {
            sourceFiles.addElement(file);
        }
        
        updateButtons();
    }
    
    private void sourcesSelected() {
        int[] selectedIndices = sourcesList.getSelectedIndices();
        if (selectedIndices.length > 0) {
            removeButton.setEnabled(true);
        } else {
            removeButton.setEnabled(false);
        }
    }
    
    private void removeSources() {
        int[] selectedIndices = sourcesList.getSelectedIndices();
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
            sourceFiles.removeElementAt(selectedIndices[i]);
        }
        updateButtons();
    }
    
    private void browseDestination() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File("C:/LocalData"));
        if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            destDir = fileChooser.getSelectedFile();
            destDirText.setText(destDir.getAbsolutePath());
            updateButtons();
        }
    }
    
    private void updateButtons() {
        copyButton.setEnabled(sourceFiles.size() > 0 && destDir != null);
    }
    
    private void copyFiles() {
        //TODO
    }
}
