package customui;

import java.awt.Graphics2D;

public interface Component {
	
	Component getParent();
	
	int getX();
	
	int getY();
	
	int getWidth();
	
	int getHeight();
	
	void doLayout();
	
	void paint(Graphics2D g);

}
