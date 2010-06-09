package customui;

import java.awt.Color;
import java.awt.Graphics2D;

public class Panel extends AbstractComponent {
	
	private final int colCount;
	
	private final int rowCount;

    private final Component[][] components;

    public Panel(int colCount, int rowCount) {
    	if (colCount < 1) {
    		throw new IllegalArgumentException("Invalid number of columns: " + colCount);
    	}
    	if (rowCount < 1) {
    		throw new IllegalArgumentException("Invalid number of rows: " + colCount);
    	}
    	this.colCount = colCount;
    	this.rowCount = rowCount;
        components = new Component[colCount][rowCount];
    }

    public void addComponent(int col, int row, Component component) {
    	if (col < 0 || col > colCount) {
    		throw new IllegalArgumentException("Invalid column: " + col);
    	}
    	if (row < 0 || row > rowCount) {
    		throw new IllegalArgumentException("Invalid row: " + row);
    	}
        components[col][row] = component;
        ((AbstractComponent) component).setParent(this);
    }

    public void doLayout() {
    }

    public void paint(Graphics2D g) {
    	int x = getX();
    	int y = getY();
		int width = getWidth();
		int height = getHeight();
		g.setColor(Color.RED);
		g.drawRect(x, y, x + width, y + height);
    }

}
