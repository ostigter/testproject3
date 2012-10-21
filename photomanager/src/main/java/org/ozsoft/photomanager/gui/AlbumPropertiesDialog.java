package org.ozsoft.photomanager.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AlbumPropertiesDialog extends Dialog {

    private JTextField nameText;
    
    private JTextField dateText;
    
    private JButton okButton;
    
    private JButton cancelButton;
    
    public AlbumPropertiesDialog(JFrame parent, boolean editMode) {
        super("Create Album", parent);
        if (editMode) {
            dialog.setTitle("Edit Album Properties");
            okButton.setText("Save");
        }
    }
    
    public String getName() {
        return nameText.getText().trim();
    }
    
    public String getDate() {
        return dateText.getText().trim();
    }
    
    @Override
    protected void initUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel label = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 5, 5);
        dialog.add(label, gbc);
        
        nameText = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 5, 5, 5);
        dialog.add(nameText, gbc);
        
        label = new JLabel("Date:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 10, 5, 5);
        dialog.add(label, gbc);
        
        dateText = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 5, 5, 5);
        dialog.add(dateText, gbc);
        
        okButton = new JButton("Create");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
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
        gbc.insets = new Insets(5, 10, 5, 10);
        dialog.add(okButton, gbc);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(5, 5, 10, 10);
        dialog.add(cancelButton, gbc);
        
        dialog.pack();
        dialog.setResizable(false);
    }
    
    private void ok() {
        if (getName().isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "An album name must be specified.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        result = Dialog.OK;
        dialog.dispose();
    }
    
    private void cancel() {
        dialog.dispose();
    }

}
