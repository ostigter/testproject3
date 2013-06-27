package org.ozsoft.secs.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ServerTool {
    
    private static final String TITLE = "SECS Server Tool";
    
    private static final int DEFAULT_WIDTH = 800;
    
    private static final int DEFAULT_HEIGHT = 600;
    
    private JFrame frame;
    
    private JTextField portText;
    
    private JTextField sessionId;
    
    private JTextField modelName;

    public ServerTool() {
        initUI();
    }
    
    public static void main(String[] args) {
        new ServerTool();
    }
    
    private void initUI() {
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder("Configuration"));
        
        JLabel label = new JLabel("Port:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        panel.add(label, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        frame.getContentPane().add(panel, gbc);
        
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
