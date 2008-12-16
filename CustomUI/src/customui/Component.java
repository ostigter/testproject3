package customui;


public interface Component {
	
	
	Component getParent();
	
	int getX();
	
	void setX(int x);
	
	int getY();
	
	void setY(int y);

	int getWidth();
	
	void setWidth(int width);
	
	int getHeight();
	
	void setHeight(int height);
	

}
