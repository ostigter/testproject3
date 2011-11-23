package customui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Frame extends AbstractComponent {

    private final JFrame frame;

    private Component content;

    public Frame(String title) {
        frame = new CustomJFrame(title);
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public void setContent(Component content) {
        this.content = content;
        ((AbstractComponent) content).setParent(this);
        setValid(false);
    }

    public void center() {
        frame.setLocationRelativeTo(null);
        setLocation(frame.getX(), frame.getY());
    }

    public void setVisible(boolean isVisible) {
        frame.setVisible(isVisible);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        frame.setSize(width, height);
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        frame.setLocation(x, y);
    }

    @Override
    public void doLayout(Graphics2D g) {
        if (content != null) {
            content.doLayout(g);
        }
        setSize(content.getWidth() + 1, content.getHeight() + 1);
        setValid(true);
    }

    @Override
    public void paint(Graphics2D g) {
        if (!isValid()) {
            doLayout(g);
        }

        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        if (content != null) {
            content.paint(g);
        }
    }

    private void close() {
        frame.dispose();
    }

    /**
     * Customized JFrame that redirects the <code>paintComponent</code> method
     * to the parent Frame.
     * 
     * @author Oscar Stigter
     */
    private class CustomJFrame extends JFrame {

        private static final long serialVersionUID = -367946099178660851L;

        public CustomJFrame(String title) {
            super(title);
            setUndecorated(true);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Frame.this.close();
                }
            });
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            Frame.this.paint((Graphics2D) g);
        }
    }
}
