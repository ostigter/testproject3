package customui;

import java.awt.Graphics2D;

public interface Component {
	
	Component getParent();
	
	int getWidth();
	
	int getHeight();
	
	int getX();
	
	int getY();
	
	void paint(Graphics2D g);

}
