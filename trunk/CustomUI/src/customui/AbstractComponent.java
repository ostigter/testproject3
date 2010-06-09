package customui;

public abstract class AbstractComponent implements Component {

	private Panel parent;

	private int x;

	private int y;

	private int width;

	private int height;

	public Panel getParent() {
		return parent;
	}

	/* package */void setParent(Panel parent) {
		this.parent = parent;
	}

	public int getX() {
		return x;
	}

	/* package */void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	/* package */void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	/* package */void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	/* package */void setHeight(int height) {
		this.height = height;
	}

}
