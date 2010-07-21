package org.ozsoft.backup;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CreateProjectDialog extends Dialog {
    
    private static final String TITLE = "Create Project";
    
    private JTextField nameTextField;
    
    private JList sourceFolderList;
    
    private JButton addButton;
    
    private JButton removeButton;
    
    private JTextField destFolderTextField;
    
    public CreateProjectDialog(JFrame owner) {
        super(TITLE, owner);
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
        
        nameTextField = new JTextField(15);
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
        
        sourceFolderList = new JList();
        sourceFolderList.setBorder(new EtchedBorder());
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
        
        label = new JLabel("Destination folder:");
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.NONE;
        gc.weightx = 0.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 10, 10, 5);
        dialog.add(label, gc);
        
        destFolderTextField = new JTextField();
        destFolderTextField.setEnabled(false);
        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.insets = new Insets(5, 5, 10, 5);
        dialog.add(destFolderTextField, gc);
        
        dialog.setSize(new Dimension(600, 400));
        dialog.setResizable(false);
    }
    
}
