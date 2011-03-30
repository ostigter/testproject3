package org.ozsoft.fondsbeheer.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * A panel used as canvas for drawing an image on.
 * 
 * @author Oscar Stigter
 */
public class CanvasPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private BufferedImage image;

    public void setImage(BufferedImage image) {
        if (image == null) {
            setPreferredSize(new Dimension(100, 100));
        } else {
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        }
        this.image = image;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(image, 0, 0, null);
        }
    }

}
