package org.ozsoft.mediacenter;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Popup extends JFrame {
    
    private static final long serialVersionUID = 1L;

    public Popup(String message) {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.BLACK));
        JLabel label = new JLabel("Refreshing list...");
        label.setFont(Constants.FONT);
        panel.add(label);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);
    }

}
