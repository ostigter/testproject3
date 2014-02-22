package org.ozsoft.photomanager.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.JDateComponentFactory;
import net.sourceforge.jdatepicker.JDatePicker;

import org.ozsoft.photomanager.entities.Album;
import org.ozsoft.photomanager.gui.util.Dialog;

public class AlbumPropertiesDialog extends Dialog {
    
    private JTextField nameText;
    
    private JDatePicker datePicker;
    
    private JButton okButton;
    
    private JButton cancelButton;
    
    public AlbumPropertiesDialog(JFrame parent) {
        this(parent, null);
    }
    
    public AlbumPropertiesDialog(JFrame parent, Album album) {
        super("Create Album", parent);
        if (album != null) {
            dialog.setTitle("Edit Album Properties");
            nameText.setText(album.getName());
//            datePicker.getModel().setValue(album.getDate());
            okButton.setText("Save");
        }
    }
    
    public String getName() {
        return nameText.getText().trim();
    }
    
    public Date getDate() {
        Date date = (Date) datePicker.getModel().getValue();
        if (date == null) {
            date = new Date();
        }
        return date;
    }
    
    @Override
    @SuppressWarnings("unchecked")
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
        
        DateModel<Date> dateModel = (DateModel<Date>) JDateComponentFactory.createDateModel(Date.class);
        datePicker = JDateComponentFactory.createJDatePicker(dateModel);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 5, 5, 5);
        dialog.add((JComponent) datePicker, gbc);
        
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
