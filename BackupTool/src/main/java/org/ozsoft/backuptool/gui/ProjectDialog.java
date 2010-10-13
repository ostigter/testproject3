// This file is part of the BackupTool project.
//
// Copyright 2010 Oscar Stigter
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.ozsoft.backuptool.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 * Dialog for creating and editing a project.
 * 
 * @author Oscar Stigter
 */
public class ProjectDialog extends Dialog {
    
    private static final String TITLE = "Create Project";
    
    private static final Font MONOSPACED_FONT = new Font("Monospaced", Font.PLAIN, 14);
    
//    private final boolean editMode;
    
    private JTextField nameTextField;
    
    private DefaultListModel sourceFolders; 
    
    private JList sourceFolderList;
    
    private JButton addButton;
    
    private JButton removeButton;
    
    private JTextField destFolderTextField;
    
    private JButton browseButton;
    
    private JButton okButton;
    
    private JButton cancelButton;
    
    public ProjectDialog(JFrame owner, boolean editMode) {
        super(TITLE, owner);
        if (editMode) {
            nameTextField.setEditable(false);
            destFolderTextField.setEditable(false);
            browseButton.setEnabled(false);
        }
    }
    
    public String getName() {
        return nameTextField.getText().trim();
    }
    
    public void setName(String name) {
        nameTextField.setText(name);
    }
    
    public Set<String> getSourceFolders() {
        Set<String> folders = new TreeSet<String>();
        Enumeration<?> enum1 = sourceFolders.elements();
        while (enum1.hasMoreElements()) {
            folders.add((String) enum1.nextElement());
        }
        return folders;
    }
    
    public void setSourceFolders(Set<String> folders) {
        sourceFolders.clear();
        for (String folder : folders) {
            sourceFolders.addElement(folder);
        }
    }
    
    public String getDestinationFolder() {
        return destFolderTextField.getText().trim();
    }
    
    public void setDestinationFolder(String folder) {
        destFolderTextField.setText(folder);
    }
    
    protected void createUI() {
        GridBagConstraints gc = new GridBagConstraints();
        
        JLabel label = new JLabel("Name:");
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 10, 5, 5);
        dialog.add(label, gc);
        
        nameTextField = new JTextField(20);
        nameTextField.setFont(MONOSPACED_FONT);
        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(10, 5, 5, 10);
        dialog.add(nameTextField, gc);
        
        JPanel sourceFolderPanel = new JPanel(new GridBagLayout());
        sourceFolderPanel.setBorder(new TitledBorder("Source folders"));
        
        sourceFolders = new DefaultListModel();
        sourceFolderList = new JList(sourceFolders);
        sourceFolderList.setBorder(new EtchedBorder());
        sourceFolderList.setFont(MONOSPACED_FONT);
        sourceFolderList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 2;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.insets = new Insets(5, 5, 5, 5);
        sourceFolderPanel.add(sourceFolderList, gc);
        
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        
        addButton = new JButton("Add...");
        addButton.setPreferredSize(new Dimension(100, 25));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addSourceFolder();
            }
        });
        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        buttonPanel.add(addButton, gc);
        
        removeButton = new JButton("Remove");
        removeButton.setPreferredSize(new Dimension(100, 25));
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeSourceFolder();
            }
        });
        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        buttonPanel.add(removeButton, gc);
        
        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.NORTH;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        sourceFolderPanel.add(buttonPanel, gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.insets = new Insets(5, 5, 5, 5);
        dialog.add(sourceFolderPanel, gc);
        
        JPanel destFolderPanel = new JPanel(new GridBagLayout());
        destFolderPanel.setBorder(new TitledBorder("Destination folder"));
        
        destFolderTextField = new JTextField();
        destFolderTextField.setFont(MONOSPACED_FONT);
        destFolderTextField.setBackground(Color.WHITE);
        destFolderTextField.setEditable(false);
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        destFolderPanel.add(destFolderTextField, gc);
        
        browseButton = new JButton("Browse...");
        browseButton.setPreferredSize(new Dimension(100, 25));
        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseDestFolder();
            }
        });
        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        destFolderPanel.add(browseButton, gc);
        
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        dialog.add(destFolderPanel, gc);
        
        JPanel southPanel = new JPanel(new GridBagLayout());
        
        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(100, 25));
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 10, 5);
        southPanel.add(okButton, gc);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 25));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 10, 5);
        southPanel.add(cancelButton, gc);
        
        gc.gridx = 0;
        gc.gridy = 3;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 5, 5);
        dialog.add(southPanel, gc);

        dialog.setSize(new Dimension(600, 400));
        dialog.setResizable(false);
    }
    
    private void addSourceFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setMultiSelectionEnabled(true);
        int result = fileChooser.showOpenDialog(owner);
        if (result == JFileChooser.APPROVE_OPTION) {
            for (File dir : fileChooser.getSelectedFiles()) {
                String path = dir.getAbsolutePath();
                if (!sourceFolders.contains(path)) {
                    sourceFolders.addElement(path);
                }
            }
        }
    }
    
    private void removeSourceFolder() {
        for (int index : sourceFolderList.getSelectedIndices()) {
            sourceFolders.remove(index);
        }
    }
    
    private void browseDestFolder() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(owner);
        if (result == JFileChooser.APPROVE_OPTION) {
            File dir = fileChooser.getSelectedFile();
            if (dir != null) {
                destFolderTextField.setText(dir.getAbsolutePath());
            }
        }
    }
    
    private void ok() {
        result = OK;
        dialog.dispose();
    }
    
    private void cancel() {
        result = CANCEL;
        dialog.dispose();
    }
    
}
