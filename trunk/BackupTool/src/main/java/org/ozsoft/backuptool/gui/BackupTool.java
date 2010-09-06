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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.ozsoft.backuptool.Project;

/**
 * Simple backup tool with a Swing UI. <br />
 * <br />
 * 
 * Supports multiple projects and incremental backups. <br />
 * <br />
 * 
 * A project backups one or more source folders to a destination folder. <br />
 * <br />
 * 
 * The backup'ed files are stored in a single binary file in the project's
 * destination folder, versioned per backup (only if new or updated). <br />
 * 
 * To maximize performance, determining whether a file has been updated is based
 * purely on the file's last modification date.
 * 
 * The project settings are stored in the 'project.dat' file in the tool's
 * installation folder. <br />
 * <br />
 * 
 * @author Oscar Stigter
 */
public class BackupTool {
    
    private static final String TITLE = "Backup Tool";
    
    private static final File PROJECTS_FILE = new File("projects.dat");
    
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    private final Map<String, Project> projects;
    
    private JFrame frame;
    
    private JTree projectTree;
    
    private DefaultMutableTreeNode projectsNode;
    
    private JPopupMenu rootMenu;

    private JPopupMenu projectMenu;
    
    private JPopupMenu backupMenu;
    
    public BackupTool() {
        projects = new TreeMap<String, Project>();
        readProjects();
        createUI();
    }

    public static void main(String[] args) {
        new BackupTool();
    }
    
    private void createUI() {
        // Create frame.
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                writeProjects();
            }
        });
        frame.setLayout(new GridBagLayout());
        
        // Create project tree.
        projectsNode = new DefaultMutableTreeNode();
        TreeModel treeModel = new DefaultTreeModel(projectsNode);
        projectTree = new JTree(treeModel);
        projectTree.setAutoscrolls(true);
        projectTree.setShowsRootHandles(true);
        JScrollPane treePane = new JScrollPane(projectTree);
        treePane.setPreferredSize(new Dimension(300, 400));
        
        // Create context menus for the project tree.
        rootMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Create Project...");
        menuItem.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
               createProject();
            } 
        });
        rootMenu.add(menuItem);
        projectMenu = new JPopupMenu();
        menuItem = new JMenuItem("Edit Project...");
        menuItem.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
               editProject();
            } 
        });
        projectMenu.add(menuItem);
        menuItem = new JMenuItem("Delete Project");
        menuItem.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
               deleteProject();
            } 
        });
        projectMenu.add(menuItem);
        projectMenu.addSeparator();
        menuItem = new JMenuItem("Create Backup...");
        menuItem.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
               createBackup();
            } 
        });
        projectMenu.add(menuItem);
        
        backupMenu = new JPopupMenu();
        menuItem = new JMenuItem("View Backup...");
        menuItem.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
               viewBackup();
            } 
        });
        backupMenu.add(menuItem);
        menuItem = new JMenuItem("Restore Backup...");
        menuItem.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
               restoreBackup();
            } 
        });
        backupMenu.add(menuItem);
        menuItem = new JMenuItem("Delete Backup");
        menuItem.addActionListener(new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e) {
               deleteBackup();
            } 
        });
        backupMenu.add(menuItem);
        projectTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showProjectTreeContextMenu(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                showProjectTreeContextMenu(e);
            }
        });
        
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        frame.getContentPane().add(treePane, gc);
        
        projectsNode.setUserObject("Projects");
        updateProjectTree();
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void updateProjectTree() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                projectsNode.removeAllChildren();
                for (Project project : projects.values()) {
                    DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(project.getName());
                    for (long date : project.getBackups()) {
                        projectNode.add(new DefaultMutableTreeNode(DATE_FORMAT.format(date)));
                    }
                    projectsNode.add(projectNode);
                }
                projectTree.updateUI();
            }
        });
    }
    
    private void showProjectTreeContextMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int x = e.getX();
            int y = e.getY();
            TreePath path = projectTree.getPathForLocation(x, y);
            if (path != null) {
                projectTree.setSelectionPath(path);
                switch (path.getPathCount()) {
                    case 1:
                        rootMenu.show(projectTree, x, y);
                        break;
                    case 2:
                        projectMenu.show(projectTree, x, y);
                        break;
                    case 3:
                        backupMenu.show(projectTree, x, y);
                        break;
                    default:
                        throw new IllegalStateException("Invalid tree depth");
                }
            }
        }
    }

    private void readProjects() {
        projects.clear();
        if (PROJECTS_FILE.isFile()) {
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(PROJECTS_FILE));
                int nofProjects = dis.readInt();
                for (int i = 0; i < nofProjects; i++) {
                    String name = dis.readUTF();
                    Project project = new Project(name);
                    int nofFolders = dis.readInt();
                    for (int j = 0; j < nofFolders; j++) {
                        project.addSourceFolder(dis.readUTF());
                    }
                    project.setDestinationFolder(dis.readUTF());
                    project.readIndexFile();
                    projects.put(name, project);
                }
            } catch (IOException e) {
                System.err.println("ERROR: " + e.getMessage());
            } finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e2) {
                        // Best effort; ignore.
                    }
                }
            }
        }
    }
    
    private void writeProjects() {
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(PROJECTS_FILE));
            dos.writeInt(projects.size());
            for (Project project : projects.values()) {
                dos.writeUTF(project.getName());
                Set<String> sourceFolders = project.getSourceFolders();
                dos.writeInt(sourceFolders.size());
                for (String folder : sourceFolders) {
                    dos.writeUTF(folder);
                }
                dos.writeUTF(project.getDestinationFolder());
            }
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    // Best effort; ignore.
                }
            }
        }
    }
    
    private void createProject() {
        ProjectDialog dialog = new ProjectDialog(frame);
        if (dialog.show() == Dialog.OK) {
            // Create project.
            String name = dialog.getName();
            Project project = new Project(name);
            for (String folder : dialog.getSourceFolders()) {
                project.addSourceFolder(folder);
            }
            project.setDestinationFolder(dialog.getDestinationFolder());
            projects.put(name, project);
            
            // Update project tree.
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(name);
            projectsNode.add(node);
            projectTree.updateUI();
        }
    }
    
    private void editProject() {
        //TODO: Edit Project
    }
    
    private void deleteProject() {
        //TODO: Delete Project
    }
    
    private void createBackup() {
        TreePath path = projectTree.getSelectionPath();
        if (path != null) {
            String name = path.getLastPathComponent().toString();
            Project project = projects.get(name);
            if (project != null) {
                project.createBackup();
                updateProjectTree();
                JOptionPane.showMessageDialog(null,
                        "Backup created successfully.", "BackupTool", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void viewBackup() {
        //TODO: View Backup
    }
    
    private void restoreBackup() {
        //TODO: Restore Backup
    }
    
    private void deleteBackup() {
        //TODO: Delete Backup
    }
    
}
