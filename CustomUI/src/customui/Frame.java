package customui;


import java.awt.Graphics;

import javax.swing.JPanel;


public class Frame extends JPanel {
	
	
	private static final long serialVersionUID = 1L;
	
	private final Panel panel;
	
	
	public Frame() {
		panel = new Panel();
		initUI();
	}
	
	
	private void initUI() {
		Label label1 = new Label("Label One");
		Label label2 = new Label("Label Two");
		panel.addComponent(label1);
		panel.addComponent(label2);
	}
	
	
	public void doLayout() {
		panel.doLayout();
	}
	
	
	@Override // JComponent
	public void setSize(int width, int height) {
		super.setSize(width, height);
		panel.setWidth(width);
		panel.setHeight(height);
	}
	
	@Override // JComponent
	protected void paintComponent(Graphics g) {
		panel.paintComponent(g);
	}
	
	

}
