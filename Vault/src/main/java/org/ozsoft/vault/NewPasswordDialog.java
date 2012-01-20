package org.ozsoft.vault;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class NewPasswordDialog extends Dialog {

    private static final long serialVersionUID = -3601949060145211322L;

    private JTextField passwordText;

    private JTextField againText;
    
    private JButton okButton;
    
    private JButton cancelButton;
    
    public NewPasswordDialog(JFrame parent) {
        super(parent);
    }
    
    public String getPassword() {
        return passwordText.getText();
    }

    @Override
    protected void initUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel label = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 5, 5, 5);
        getContentPane().add(label, gbc);
        
        passwordText = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 5, 10);
        getContentPane().add(passwordText, gbc);
        
        label = new JLabel("Again:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(label, gbc);
        
        againText = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        getContentPane().add(againText, gbc);
        
        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 10, 10, 5);
        getContentPane().add(okButton, gbc);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 5, 10, 10);
        getContentPane().add(cancelButton, gbc);
    }

    @Override
    protected boolean validationOK() {
        boolean validationOK = false;
        String password = passwordText.getText();
        String again = againText.getText();
        if (password.length() == 0) {
            //TODO
        } else {
            if (!again.equals(password)) {
                //TODO
            } else {
                validationOK = true;
            }
        }
        return validationOK;
    }
    
}
