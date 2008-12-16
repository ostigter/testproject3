package customui;


import java.awt.Graphics;


public interface Component {
	
	
	Component getParent();
	
	int getX();
	
	int getY();
	
	int getWidth();
	
	int getHeight();
	
	void doLayout();
	
	void paintComponent(Graphics g);
	

}
