package org.ozsoft.photomanager.gui.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * Layout manager that lays out components in a grid of equally sized rows and columns, starting in the top row and the
 * most left column, to the next column to the right until the container's width is reached, then wrapping to the next
 * row in the most left column again, automatically adding a vertical scrollbar when needed. <br />
 * <br />
 * 
 * If the container is resized, the <code>revalidate</code> method should be called to update the wrapping according to
 * the container's new width and height.
 * 
 * @author Oscar Stigter
 */
public class GalleryLayout implements LayoutManager {

    private static final Dimension MIN_LAYOUT_SIZE = new Dimension(100, 100);

    @Override
    public void layoutContainer(Container container) {
        int totalWidth = container.getParent().getWidth();
        int totalHeight = 0;
        int rowHeight = 0;
        int x = 0;
        int y = 0;
        boolean newRow = false;
        for (Component component : container.getComponents()) {
            Dimension size = component.getPreferredSize();
            int width = (int) Math.floor(size.getWidth());
            int height = (int) Math.floor(size.getHeight());
            if ((x + width) < totalWidth) {
                // Component fits on current row.
                component.setBounds(x, y, width, height);
                x += width;
                if (newRow) {
                    totalHeight += rowHeight;
                    newRow = false;
                }
            } else {
                // Add component to new row.
                newRow = true;
                x = 0;
                y += rowHeight;
                totalHeight += rowHeight;
                component.setBounds(x, y, width, height);
            }
            if (height > rowHeight) {
                rowHeight = height;
            }
        }
        if (!newRow) {
            totalHeight += rowHeight;
        }
        container.setPreferredSize(new Dimension(totalWidth, totalHeight));
    }

    @Override
    public void addLayoutComponent(String directions, Component component) {
        // Not implemented.
    }

    @Override
    public void removeLayoutComponent(Component component) {
        // Not implemented.
    }

    @Override
    public Dimension preferredLayoutSize(Container container) {
        return MIN_LAYOUT_SIZE;
    }

    @Override
    public Dimension minimumLayoutSize(Container container) {
        return MIN_LAYOUT_SIZE;
    }

}
