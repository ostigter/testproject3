package org.ozsoft.photomanager.gui;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PhotoPanel extends JPanel {

    private static final long serialVersionUID = -3294558899400031309L;
    
    private final JLabel label;

    public PhotoPanel() {
        label = new JLabel();
        add(label);
    }

    public void setPhoto(byte[] content) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(content));
            label.setIcon(new ImageIcon(image));
            setSize(image.getWidth(), image.getHeight());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
