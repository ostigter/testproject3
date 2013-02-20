package org.ozsoft.photomanager.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class GalleryLayout implements LayoutManager {
    
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
        return new Dimension(100, 100);
    }

    @Override
    public Dimension minimumLayoutSize(Container container) {
        return new Dimension(100, 100);
    }

}
