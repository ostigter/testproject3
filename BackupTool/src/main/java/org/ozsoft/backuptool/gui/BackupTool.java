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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.ozsoft.backuptool.Backup;
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
    
    private final Map<String, Project> projects;
    
    private JFrame frame;
    
    private JTree projectTree;
    
    private DefaultMutableTreeNode projectsNode;
    
    private JPopupMenu rootMenu;

    private JPopupMenu projectMenu;
    
    private JPopupMenu backupMenu;
    
    /**
     * Constructor.
     */
    public BackupTool() {
        projects = new TreeMap<String, Project>();
        readProjects();
        createUI();
    }

    /**
     * Application entry point.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        new BackupTool();
    }
    
    /**
     * Creates the UI.
     */
    private void createUI() {
        // Create frame.
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        
        // Root menu.
        rootMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Create Project...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               createProject();
            } 
        });
        rootMenu.add(menuItem);
        
        // Project menu.
        projectMenu = new JPopupMenu();
        menuItem = new JMenuItem("Create Backup...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               createBackup();
            } 
        });
        projectMenu.add(menuItem);
        projectMenu.addSeparator();
        menuItem = new JMenuItem("Edit Project...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               editProject();
            } 
        });
        projectMenu.add(menuItem);
        menuItem = new JMenuItem("Delete Project");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               deleteProject();
            } 
        });
        projectMenu.add(menuItem);
        projectMenu.addSeparator();
        menuItem = new JMenuItem("Compact Archive");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               compactArchive();
            } 
        });
        projectMenu.add(menuItem);
        
        // Backup menu.
        backupMenu = new JPopupMenu();
        menuItem = new JMenuItem("View Backup...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               viewBackup();
            } 
        });
        backupMenu.add(menuItem);
        menuItem = new JMenuItem("Restore Backup...");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               restoreBackup();
            } 
        });
        backupMenu.add(menuItem);
        backupMenu.addSeparator();
        menuItem = new JMenuItem("Delete Backup");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               deleteBackup();
            } 
        });
        backupMenu.add(menuItem);
        
        projectTree.addMouseListener(new MouseAdapter() {
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
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        updateProjectTree();
    }
    
    /**
     * Updates the project tree.
     */
    private void updateProjectTree() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                projectsNode.removeAllChildren();
                for (Project project : projects.values()) {
                    DefaultMutableTreeNode projectNode = new DefaultMutableTreeNode(project);
                    for (Backup backup : project.getBackups()) {
                        projectNode.add(new DefaultMutableTreeNode(backup));
                    }
                    projectsNode.add(projectNode);
                }
                expandTree(projectTree, true);
                projectTree.updateUI();
            }
        });
    }
    
    /**
     * Shows the project tree's context menu, based on the type of the selected
     * node (root, project or backup).
     * 
     * @param e
     *            The mouse event that triggered the context menu.
     */
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

    /**
     * Reads the project information from the file system.
     */
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
                    Set<String> sourceFolders = new TreeSet<String>();
                    for (int j = 0; j < nofFolders; j++) {
                        sourceFolders.add(dis.readUTF());
                    }
                    project.setSourceFolders(sourceFolders);
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
    
    /**
     * Writes the project information to the file system.
     */
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
    
    /**
     * Creates a new project.
     */
    private void createProject() {
        ProjectDialog dialog = new ProjectDialog(frame, false);
        if (dialog.show() == Dialog.OK) {
            // Create project.
            String name = dialog.getName();
            Project project = new Project(name);
            project.setSourceFolders(dialog.getSourceFolders());
            project.setDestinationFolder(dialog.getDestinationFolder());
            projects.put(name, project);
            writeProjects();
            updateProjectTree();
        }
    }
    
    /**
     * Edits the selected project.
     */
    private void editProject() {
        TreePath path = projectTree.getSelectionPath();
        if (path != null) {
            Project project = (Project) ((DefaultMutableTreeNode) path.getPathComponent(1)).getUserObject();
            ProjectDialog dialog = new ProjectDialog(frame, true);
            dialog.setName(project.getName());
            dialog.setSourceFolders(project.getSourceFolders());
            dialog.setDestinationFolder(project.getDestinationFolder());
            if (dialog.show() == Dialog.OK) {
                project.setSourceFolders(dialog.getSourceFolders());
                writeProjects();
            }
        }
    }
    
    /**
     * Deletes the selected project.
     */
    private void deleteProject() {
        //TODO: Implement Delete Project.
        JOptionPane.showMessageDialog(frame,
                "This functionality has not been implemented yet.", "BackupTool", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Creates a backup of the selected project.
     */
    private void createBackup() {
        TreePath path = projectTree.getSelectionPath();
        if (path != null) {
            Project project = (Project) ((DefaultMutableTreeNode) path.getPathComponent(1)).getUserObject();
            try {
                project.createBackup();
                updateProjectTree();
                JOptionPane.showMessageDialog(frame,
                        "Backup created successfully.", "BackupTool", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                        "Error creating backup: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Views the contents of the selected backup.
     */
    private void viewBackup() {
        //TODO: Implement View Project.
        JOptionPane.showMessageDialog(
                frame, "This functionality has not been implemented yet.", "BackupTool", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Restores the selected backup.
     */
    private void restoreBackup() {
        TreePath path = projectTree.getSelectionPath();
        if (path != null) {
            Project project = (Project) ((DefaultMutableTreeNode) path.getPathComponent(1)).getUserObject();
            Backup backup = (Backup) ((DefaultMutableTreeNode) path.getPathComponent(2)).getUserObject();
            try {
                project.restoreBackup(backup.getId());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                        "Error restoring file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Deletes the selected backup.
     */
    private void deleteBackup() {
        TreePath path = projectTree.getSelectionPath();
        if (path != null) {
            Project project = (Project) ((DefaultMutableTreeNode) path.getPathComponent(1)).getUserObject();
            Backup backup = (Backup) ((DefaultMutableTreeNode) path.getPathComponent(2)).getUserObject();
            String[] options = new String[] {"Delete", "Cancel"};
            int result = JOptionPane.showOptionDialog(frame,
                    String.format("Delete backup of project '%s' with date %s?", project, backup),
                    "BackupTool", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (result == JOptionPane.OK_OPTION) {
                project.deleteBackup(backup.getId());
                updateProjectTree();
            }
        }
    }
    
    /**
     * Compacts the project's archive file.
     */
    private void compactArchive() {
        TreePath path = projectTree.getSelectionPath();
        if (path != null) {
            Project project = (Project) ((DefaultMutableTreeNode) path.getPathComponent(1)).getUserObject();
            try {
                project.compactArchive();
                JOptionPane.showMessageDialog(frame,
                        "Archive file compacted successfully.", "Backup Tool", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame,
                        "Error compacting archive file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Expands or collapses all nodes of a <code>JTree</code>.
     * 
     * @param tree
     *            The <code>JTree</code>.
     * @param expand
     *            If true, expands the nodes, otherwise collapses them.
     */
    private static void expandTree(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandTreeNode(tree, new TreePath(root), expand);
    }
    
    /**
     * Expands or collapses a single <code>JTree</code> node.
     * 
     * @param tree
     *            The <code>JTree</code>.
     * @param parent
     *            The node.
     * @param expand
     *            If true, expands the nodes, otherwise collapses them.
     */
    private static void expandTreeNode(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        for (Enumeration<?> e = node.children(); e.hasMoreElements(); ) {
            TreeNode childNode = (TreeNode) e.nextElement();
            expandTreeNode(tree, parent.pathByAddingChild(childNode), expand);
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }     
    
}
