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
    	if (col < 0 || col > (colCount - 1)) {
    		throw new IllegalArgumentException("Invalid column: " + col);
    	}
    	if (row < 0 || row > (rowCount - 1)) {
    		throw new IllegalArgumentException("Invalid row: " + row);
    	}
        components[col][row] = component;
        ((AbstractComponent) component).setParent(this);
        setValid(false);
    }

    public void doLayout() {
    	// Calculate total width of all columns.
    	int width = 0;
    	for (int col = 0; col < colCount; col++) {
    		int colWidth = 0;
    		for (int row = 0; row < rowCount; row++) {
        		Component component = components[col][row];
        		if (component != null) {
        			int componentWidth = component.getWidth();
        			if (componentWidth > colWidth) {
            			colWidth = componentWidth; 
        			}
        		}
        	}
    		width += colWidth;
    	}

    	// Calculate total height of all rows.
    	int height = 0;
		for (int row = 0; row < rowCount; row++) {
    		int rowHeight = 0;
        	for (int col = 0; col < colCount; col++) {
        		Component component = components[col][row];
        		if (component != null) {
        			int componentHeight = component.getHeight();
        			if (componentHeight > rowHeight) {
            			rowHeight = componentHeight; 
        			}
        		}
        	}
    		height += rowHeight;
    	}
		
		setSize(width, height);
		
		setValid(true);
    }

    public void paint(Graphics2D g) {
    	if (!isValid()) {
    		doLayout();
    	}
    	
    	int x = getX();
    	int y = getY();
		int width = getWidth();
		int height = getHeight();
		
		g.setColor(Color.BLUE);
		g.drawRect(x, y, x + width, y + height);
    }

}
