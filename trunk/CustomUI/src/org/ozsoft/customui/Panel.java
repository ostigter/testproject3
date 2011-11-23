package org.ozsoft.customui;

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

    public void doLayout(Graphics2D g) {
        if (!isValid()) {
            // Calculate column widths.
            int[] colWidths = new int[colCount];
            int totalWidth = 0;
            for (int col = 0; col < colCount; col++) {
                int colWidth = 0;
                for (int row = 0; row < rowCount; row++) {
                    Component component = components[col][row];
                    if (component != null) {
                        component.doLayout(g);
                        int componentWidth = component.getWidth();
                        if (componentWidth > colWidth) {
                            colWidth = componentWidth;
                        }
                    }
                }
                colWidths[col] = colWidth;
                totalWidth += colWidth;
            }
    
            // Calculate row heights.
            int[] rowHeights = new int[rowCount];
            int totalHeight = 0;
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
                rowHeights[row] = rowHeight;
                totalHeight += rowHeight;
            }
    
            int x = getX();
            int y = getY();
    
            int colX[] = new int[colCount];
            int x2 = x;
            for (int col = 0; col < colCount; col++) {
                colX[col] = x2;
                x2 += colWidths[col];
            }
    
            int rowY[] = new int[rowCount];
            int y2 = y;
            for (int row = 0; row < rowCount; row++) {
                rowY[row] = y2;
                y2 += rowHeights[row];
            }
    
            // Distribute any left over space equally.
            int parentWidth = getParent().getWidth();
            if (parentWidth > totalWidth) {
                int remainder = parentWidth - totalWidth;
                for (int col = 0; col < colCount; col++) {
                    colWidths[col] += (remainder / colCount);
                }
                totalWidth = parentWidth;
            }
            int parentHeight = getParent().getHeight();
            if (parentHeight > totalHeight) {
                int remainder = parentHeight - totalHeight;
                for (int row = 0; row < rowCount; row++) {
                    rowHeights[row] += (remainder / rowCount);
                }
                totalHeight = parentHeight;
            }
    
            // Set component size and position.
            for (int row = 0; row < rowCount; row++) {
                for (int col = 0; col < colCount; col++) {
                    AbstractComponent component = (AbstractComponent) components[col][row];
                    if (component != null) {
                        component.setLocation(colX[col], rowY[row]);
                    }
                }
            }
    
            setSize(totalWidth, totalHeight);
    
            setValid(true);
        }
    }

    public void paint(Graphics2D g) {
        doLayout(g);
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                Component component = components[col][row];
                if (component != null) {
                    component.paint(g);
                }
            }
        }
    }

}
