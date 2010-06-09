package customui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class Frame extends JPanel {

    private static final long serialVersionUID = 1L;
    
    private Panel panel;

    public Frame() {
        initUI();
    }

    private void initUI() {
    	Font font = new Font("Arial", Font.PLAIN, 12);
    	Panel panel = new Panel(2, 1);
        Label label1 = new Label("Label 1", font);
        Label label2 = new Label("Label 2", font);
        panel.addComponent(0, 0, label1);
        panel.addComponent(1, 0, label2);
        setPanel(panel);
    }
    
    public void setPanel(Panel panel) {
    	this.panel = panel;
    }

    public void doLayout() {
        panel.doLayout();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        panel.setWidth(width);
        panel.setHeight(height);
    }

    @Override
    protected void paintComponent(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g;
    	super.paintComponent(g2d);
        panel.paint(g2d);
    }

}
