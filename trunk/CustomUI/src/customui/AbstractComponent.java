package customui;


public abstract class AbstractComponent implements Component {
	

	private Container parent;
	
	private int x;
	
	private int y;
	
	private int width;
	
	private int height;
	
	
	public AbstractComponent(Container parent) {
		this.parent = parent;
	}


	public Component getParent() {
		return parent;
	}
	
	
	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}

	
	public int getY() {
		return y;
	}

	
	public void setY(int y) {
		this.y = y;
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}

	
	public void setHeight(int height) {
		this.height = height;
	}


}
