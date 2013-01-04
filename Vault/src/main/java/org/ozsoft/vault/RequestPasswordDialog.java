package org.ozsoft.vault;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class RequestPasswordDialog extends Dialog {

    private static final long serialVersionUID = -3601949060145211322L;

    private JPasswordField passwordText;

    private JButton okButton;
    
    private JButton cancelButton;
    
    public RequestPasswordDialog(JFrame parent) {
        super(parent, "Enter password");
    }
    
    public String getPassword() {
        return new String(passwordText.getPassword());
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
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 5, 5, 5);
        getContentPane().add(label, gbc);
        
        passwordText = new JPasswordField(10);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(10, 5, 5, 5);
        getContentPane().add(passwordText, gbc);
        
        okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 1;
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
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 5, 10, 10);
        getContentPane().add(cancelButton, gbc);
        
        getRootPane().setDefaultButton(okButton);
    }

    @Override
    protected boolean validationOK() {
        boolean validationOK = false;
        String password = new String(passwordText.getPassword());
        if (password.length() > 0) {
            validationOK = true;
        } else {
            JOptionPane.showMessageDialog(this, "No password specified. Please enter the password.", "Vault", JOptionPane.ERROR_MESSAGE);
        }
        return validationOK;
    }
    
}
