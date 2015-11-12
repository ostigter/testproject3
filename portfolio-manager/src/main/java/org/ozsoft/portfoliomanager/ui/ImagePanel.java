package org.ozsoft.portfoliomanager.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.apache.commons.io.IOUtils;

public class ImagePanel extends JPanel {

    private static final long serialVersionUID = -7868161566551066062L;

    private BufferedImage image;

    public void setImage(InputStream is) throws IOException {
        if (is == null) {
            throw new IllegalArgumentException("Null input stream");
        }

        try {
            image = ImageIO.read(is);
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }
}
